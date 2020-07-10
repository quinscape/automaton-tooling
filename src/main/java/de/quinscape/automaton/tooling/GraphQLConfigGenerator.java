package de.quinscape.automaton.tooling;

import de.quinscape.automaton.tooling.model.graphql.ForeignKeyRelation;
import de.quinscape.automaton.tooling.model.graphql.GraphQLConfig;
import de.quinscape.automaton.tooling.model.graphql.SourceField;
import de.quinscape.automaton.tooling.model.graphql.TargetField;
import de.quinscape.automaton.tooling.model.graphql.ViewRelation;
import de.quinscape.automaton.tooling.template.BaseTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.svenson.JSON;
import org.svenson.JSONParser;
import org.svenson.tokenize.InputStreamSource;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

@Command(name="graphql", description = "Generates a GraphQLConfiguration.java for automaton projects based on input JSON data")
public class GraphQLConfigGenerator
    implements Callable<Integer>
{

    private final static String VAR_INPUT_TYPES = "INPUT_TYPES";
    private final static String VAR_POJO_IMPORTS = "POJO_IMPORTS";
    private final static String VAR_BASE_PACKAGE = "BASE_PACKAGE";
    private final static String VAR_DESCRIPTION = "DESCRIPTION";
    private final static String VAR_FOREIGN_KEY_RELATIONS = "FOREIGN_KEY_RELATIONS";
    private final static String VAR_VIEW_RELATIONS = "VIEW_RELATIONS";
    private final static String VAR_NAME_FIELDS = "NAME_FIELDS";
    private final static String VAR_NAME_FIELDS_BY_TYPE = "NAME_FIELDS_BY_TYPE";


    @Option( names = {"-b", "--base-package"}, description = "Base package for the automaton project.", required = true)
    private String basePackage;

    @Option( names = {"-i", "--input"}, description = "Path to the input JSON data", required = true)
    private String inputPath;

    @Option( names = {"-o", "--output"}, description = "Output path for the generated Java file")
    private String outputPath;

    @Option( names = {"-t", "--template"}, description = "Custom template", required = false)
    private String templatePath;

    @Override
    public Integer call() throws Exception
    {
        final BaseTemplate template = loadTemplate();
        final GraphQLConfig config = loadInput();

        final Map<String, Object> model = new HashMap<>();

        model.put(VAR_DESCRIPTION, "Auto-generated GraphQL configuration for " + basePackage);
        model.put(VAR_BASE_PACKAGE, basePackage);

        final Set<String> imported = new TreeSet<>(config.getAdditionalInputTypes());
        

        model.put(
            VAR_INPUT_TYPES,
            reindent(
                4,
                renderInputTypes(
                    config.getAdditionalInputTypes()
                )
            )
        );

        model.put(
            VAR_FOREIGN_KEY_RELATIONS,
                reindent(
                    3,
                    renderForeignKeyRelations(
                        config.getForeignKeyRelations()
                    )
                )
        );

        model.put(
            VAR_VIEW_RELATIONS,
                reindent(
                    3,
                    renderViewRelations(
                        config.getViewRelations(),
                        imported
                    )
                )
        );

        model.put(
            VAR_NAME_FIELDS,
                reindent(
                    3,
                    renderNameFieldsConfig(
                        config.getNameFields()
                    )
                )
        );

        model.put(
            VAR_NAME_FIELDS_BY_TYPE,
                reindent(
                    3,
                    renderNameFieldsForTypeConfig(
                        config.getNameFieldsByType(),
                        imported
                    )
                )
        );

        model.put(
            VAR_POJO_IMPORTS,
            renderPOJOImports(
                imported
            )
        );

        renderResult(template, model);
        return 0;
    }


    private String renderNameFieldsForTypeConfig(
        Map<String, List<String>> nameFieldsForType,
        Set<String> imported
    )
    {
        if (nameFieldsForType.size() == 0)
        {
            return "";
        }
        final StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<String>> e : nameFieldsForType.entrySet())
        {
            final String type = e.getKey();
            final List<String> nameFields = e.getValue();

            imported.add(type);

            sb.append(".configureNameFields(").append(type).append(".class, ");


            for (Iterator<String> iterator = nameFields.iterator(); iterator.hasNext(); )
            {
                String nameField = iterator.next();
                sb.append(JSON.defaultJSON().quote(nameField));

                if (iterator.hasNext())
                {
                    sb.append(", ");
                }
            }

            sb.append(")\n");

        }

        return sb.toString();
    }


    private String renderNameFieldsConfig(List<String> nameFields)
    {
        if (nameFields.size() == 0)
        {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        sb.append(".configureNameField(");

        for (Iterator<String> iterator = nameFields.iterator(); iterator.hasNext(); )
        {
            String nameField = iterator.next();

            sb.append(
                JSON.defaultJSON().quote(nameField)
            );

            if (iterator.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(")\n");

        return sb.toString();
    }


    private String renderViewRelations(List<ViewRelation> viewRelations, Set<String> imported)
    {
        if (viewRelations.size() == 0)
        {
            return "";
        }


        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < viewRelations.size(); i++)
        {
            ViewRelation relation = viewRelations.get(i);

            sb.append(".withRelation(\n")
                .append("    new RelationBuilder()\n");

            if (relation.getId() != null)
            {
                sb.append("        .withId(").append(JSON.defaultJSON().quote(relation.getId())).append(")\n");
            }

            final String sourcePojo = relation.getSourcePojo();
            final List<String> sourceFields = relation.getSourceFields();
            final String targetPojo = relation.getTargetPojo();
            final List<String> targetFields = relation.getTargetFields();


            if (sourcePojo == null)
            {
                throw new IllegalArgumentException("View relation #" + i + ": sourcePojo can 't be null");
            }

            if (targetPojo == null)
            {
                throw new IllegalArgumentException("View relation #" + i + ": targetPojo can't be null");
            }

            if (sourceFields == null || sourceFields.size() == 0)
            {
                throw new IllegalArgumentException("View relation #" + i + ": sourceFields can't be empty or null");
            }

            if (targetFields == null || targetFields.size() == 0)
            {
                throw new IllegalArgumentException("View relation #" + i + ": targetFields can't be empty or null");
            }

            imported.add(sourcePojo);
            imported.add(targetPojo);

            if (relation.getId() != null)
            {
                sb.append("        .withId(").append(JSON.defaultJSON().quote(relation.getId())).append(")\n");
            }

            sb.append("        .withPojoFields(");
            sb.append(sourcePojo).append(".class, ");
            appendFields(sb, sourceFields);
            sb.append(", ");
            sb.append(targetPojo).append(".class, ");
            appendFields(sb, targetFields);
            sb.append(")\n");

            final String leftSideObjectName = relation.getLeftSideObjectName();
            final String rightSideObjectName = relation.getRightSideObjectName();
            final SourceField sourceField = relation.getSourceField();
            final TargetField targetField = relation.getTargetField();

            if (sourceField != SourceField.NONE)
            {
                sb.append("        .withSourceField(SourceField.").append(sourceField).append(")\n");
                if (leftSideObjectName != null)
                {
                    sb.append("        .withLeftSideObjectName(").append(JSON.defaultJSON().quote(leftSideObjectName)).append(")\n");
                }
            }

            if (targetField != TargetField.NONE)
            {
                sb.append("        .withTargetField(TargetField.").append(targetField).append(")\n");
                if (rightSideObjectName != null)
                {
                    sb.append("        .withRightSideObjectName(").append(JSON.defaultJSON().quote(rightSideObjectName)).append(")\n");
                }
            }
            sb.append(")\n");
        }

        return sb.toString();
    }


    private void appendFields(StringBuilder sb, List<String> sourceFields)
    {
        if (sourceFields.size() == 1)
        {
            sb.append("Collections.singletonList(").append(JSON.defaultJSON().quote(sourceFields.get(0))).append(")");
        }
        else
        {
            sb.append("Arrays.asList(");

            for (Iterator<String> iterator = sourceFields.iterator(); iterator.hasNext(); )
            {
                String sourceField = iterator.next();
                sb.append(JSON.defaultJSON().quote(sourceField));
                if (iterator.hasNext())
                {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
    }


    private String renderForeignKeyRelations(List<ForeignKeyRelation> foreignKeyRelations)
    {
        if (foreignKeyRelations.size() == 0)
        {
            return "";
        }


        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < foreignKeyRelations.size(); i++)
        {
            ForeignKeyRelation relation = foreignKeyRelations.get(i);
            final String fkField = relation.getFkField();
            final SourceField sourceField = relation.getSourceField();
            final String leftSideObjectName = relation.getLeftSideObjectName();
            final TargetField targetField = relation.getTargetField();
            final String rightSideObjectName = relation.getRightSideObjectName();

            if (fkField == null)
            {
                throw new IllegalArgumentException("Foreign key relation #" + i + ": fkField can't be null");
            }

            sb.append(".configureRelation(")
                .append(fkField)
                .append(", SourceField.").append(sourceField)
                .append(", TargetField.").append(targetField);

            if (leftSideObjectName != null || rightSideObjectName != null)
            {
                sb.append(", ").append(JSON.defaultJSON().quote(leftSideObjectName)).append(", ").append(JSON.defaultJSON().quote(rightSideObjectName));
            }
            sb.append(")\n");
        }

        return sb.toString();
    }


    private void renderResult(BaseTemplate template, Map<String, Object> model) throws IOException
    {
        final OutputStream os;
        if (outputPath != null)
        {
            os = new FileOutputStream(
                new File(outputPath)
            );
        }
        else
        {
            os = new ByteArrayOutputStream();
        }
        template.write(
            os,
            model
        );
        os.close();

        if (outputPath == null)
        {
            String output = new String(((ByteArrayOutputStream)os).toByteArray());
            System.out.println(output);
        }
    }


    private String renderPOJOImports(Set<String> additionalInputTypes)
    {
        final StringBuilder sb = new StringBuilder();

        for (String type : additionalInputTypes)
        {
            sb.append("import ").append(basePackage).append(".domain.tables.pojos.").append(type).append(";\n");
        }

        return sb.toString();
    }


    private String reindent(int levels, String text)
    {
        if (text == null || text.length() == 0)
        {
            return "";
        }

        final String[] lines = text.split("\n");

        final StringBuilder sb = new StringBuilder(text.length() + (lines.length - 1) * (levels * 4));

        sb.append(lines[0]).append("\n");
        for (int i = 1; i < lines.length; i++)
        {
            for (int j=0; j < levels; j++)
            {
                sb.append("    ");
            }
            sb.append(lines[i]).append("\n");
        }
        return sb.toString();
    }


    private String renderInputTypes(List<String> additionalInputTypes)
    {
        final StringBuilder sb = new StringBuilder();

        for (Iterator<String> iterator = additionalInputTypes.iterator(); iterator.hasNext(); )
        {
            String type = iterator.next();

            sb.append(type).append(".class");
            if (iterator.hasNext())
            {
                sb.append(",\n");
            }
        }
        return sb.toString();
    }


    private GraphQLConfig loadInput() throws FileNotFoundException
    {
        return JSONParser.defaultJSONParser().parse(
            GraphQLConfig.class,
            new InputStreamSource(
                new FileInputStream(
                    new File(inputPath)
                ),
                true
            )
        );
    }


    private BaseTemplate loadTemplate() throws IOException
    {
        final String template;
        if (templatePath != null)
        {
            template = FileUtils.readFileToString(new File(templatePath), "UTF-8");
        }
        else
        {
            template = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(
                "GraphQLConfigurationTemplate.java"), StandardCharsets.UTF_8);
        }

        return new BaseTemplate(template);
    }


    public static void main(String[] args)
    {
        new CommandLine(new GraphQLConfigGenerator()).execute(args);

    }
}
