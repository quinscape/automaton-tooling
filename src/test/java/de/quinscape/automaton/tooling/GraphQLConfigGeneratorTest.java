package de.quinscape.automaton.tooling;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class GraphQLConfigGeneratorTest
{
    private final static Logger log = LoggerFactory.getLogger(GraphQLConfigGeneratorTest.class);


    @Test
    void testGenerator() throws IOException
    {
        final File tmpFile = File.createTempFile("GraphQLConfig", ".java");
        tmpFile.deleteOnExit();

        AutomatonTooling.main(
            new String[] {
                "graphql",
                "-i", "src/test/java/de/quinscape/automaton/tooling/input.json",
                "-o", tmpFile.getAbsolutePath(),
                "-b", "de.quinscape.testapp",
            }
        );

        String output = FileUtils.readFileToString(tmpFile, StandardCharsets.UTF_8);

        log.info("TEMP FILE: {}", tmpFile.getAbsolutePath());

        assertThat(
            output,
            is("package de.quinscape.testapp.runtime.config;\n" +
                "\n" +
                "import de.quinscape.automaton.model.domain.AutomatonRelation;\n" +
                "import de.quinscape.automaton.runtime.domain.builder.AutomatonDomain;\n" +
                "\n" +
                "import de.quinscape.testapp.domain.Public;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.AAnlage;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.Bar;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.BeispielA;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.BeispielB;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.Foo;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.IBearbeiteteObjekteAnlagenBetriebsstaetten;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.IBeispielLink;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.IFavoritenAnlagenBetriebsstaetten;\n" +
                "import de.quinscape.testapp.domain.tables.pojos.Qux;\n" +
                "\n" +
                "import de.quinscape.domainql.DomainQL;\n" +
                "import de.quinscape.domainql.RelationBuilder;\n" +
                "import de.quinscape.domainql.annotation.GraphQLLogic;\n" +
                "import de.quinscape.domainql.config.SourceField;\n" +
                "import de.quinscape.domainql.config.TargetField;\n" +
                "import de.quinscape.domainql.meta.MetadataProvider;\n" +
                "import graphql.GraphQL;\n" +
                "import org.jooq.DSLContext;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.context.ApplicationContext;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.core.io.ClassPathResource;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.math.BigDecimal;\n" +
                "import java.util.Collection;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.Collections;\n" +
                "\n" +
                "\n" +
                "import static de.quinscape.testapp.domain.Tables.*;\n" +
                "\n" +
                "/**\n" +
                " * Auto-generated GraphQL configuration for de.quinscape.testapp\n" +
                " */\n" +
                "@Configuration\n" +
                "public class GraphQLConfiguration\n" +
                "{\n" +
                "    private final static Logger log = LoggerFactory.getLogger(GraphQLConfiguration.class);\n" +
                "\n" +
                "    private final ApplicationContext applicationContext;\n" +
                "\n" +
                "    private final DSLContext dslContext;\n" +
                "\n" +
                "\n" +
                "    @Autowired\n" +
                "    public GraphQLConfiguration(\n" +
                "        ApplicationContext applicationContext,\n" +
                "        DSLContext dslContext\n" +
                "    )\n" +
                "    {\n" +
                "        this.applicationContext = applicationContext;\n" +
                "        this.dslContext = dslContext;\n" +
                "    }\n" +
                "\n" +
                "    \n" +
                "    @Bean\n" +
                "    public DomainQL domainQL() throws IOException\n" +
                "    {\n" +
                "        final Collection<Object> logicBeans = applicationContext.getBeansWithAnnotation(GraphQLLogic" +
                ".class).values();\n" +
                "        final Collection<MetadataProvider> metadataProviders = applicationContext.getBeansOfType" +
                "(MetadataProvider.class).values();\n" +
                "\n" +
                "        final DomainQL domainQL = AutomatonDomain.newDomain(dslContext, metadataProviders)\n" +
                "\n" +
                "            .logicBeans(logicBeans)\n" +
                "\n" +
                "            .objectTypes(Public.PUBLIC)\n" +
                "\n" +
                "            .withAdditionalInputTypes(\n" +
                "                Foo.class,\n" +
                "                Bar.class\n" +
                "\n" +
                "            )\n" +
                "            \n" +
                "            // configure object creation for foreign key relationships\n" +
                "            .configureRelation(FOO.OWNER_ID, SourceField.OBJECT_AND_SCALAR, TargetField.MANY)\n" +
                "            .configureRelation(QUX_MAIN.QUX_B_NAME, SourceField.OBJECT_AND_SCALAR, TargetField.NONE," +
                " \"quxB\", null)\n" +
                "            .configureRelation(A_44_VO_ANLAGE_BETRIEBSEINHEIT.VO_44_ID, SourceField" +
                ".OBJECT_AND_SCALAR, TargetField.MANY, null, \"a_44VoAnlageBetriebseinheit\", AutomatonRelation" +
                ".MANY_TO_MANY)\n" +
                "            .configureRelation(A_44_VO_ANLAGE_BETRIEBSEINHEIT.ANLAGE_ID, SourceField" +
                ".OBJECT_AND_SCALAR, TargetField.MANY, null, \"a_44VoAnlageBetriebseinheit\", AutomatonRelation" +
                ".MANY_TO_MANY)\n" +
                "\n" +
                "            // configure object creation for view / POJO-based relationships\n" +
                "            .withRelation(\n" +
                "                new RelationBuilder()\n" +
                "                    .withPojoFields(IBearbeiteteObjekteAnlagenBetriebsstaetten.class, Collections" +
                ".singletonList(\"beaAnlageId\"), AAnlage.class, Collections.singletonList(\"id\"))\n" +
                "                    .withSourceField(SourceField.OBJECT_AND_SCALAR)\n" +
                "                    .withLeftSideObjectName(\"aAnlage\")\n" +
                "            )\n" +
                "            .withRelation(\n" +
                "                new RelationBuilder()\n" +
                "                    .withPojoFields(IFavoritenAnlagenBetriebsstaetten.class, Collections" +
                ".singletonList(\"favAnlageId\"), AAnlage.class, Collections.singletonList(\"id\"))\n" +
                "                    .withSourceField(SourceField.OBJECT_AND_SCALAR)\n" +
                "                    .withLeftSideObjectName(\"aAnlage\")\n" +
                "                    .withTargetField(TargetField.MANY)\n" +
                "                    .withRightSideObjectName(\"anlagen\")\n" +
                "            )\n" +
                "            .withRelation(\n" +
                "                new RelationBuilder()\n" +
                "                    .withPojoFields(IBeispielLink.class, Collections.singletonList(\"beispielAId\")," +
                " BeispielA.class, Collections.singletonList(\"id\"))\n" +
                "                    .withSourceField(SourceField.OBJECT_AND_SCALAR)\n" +
                "                    .withLeftSideObjectName(\"beispielA\")\n" +
                "                    .withTargetField(TargetField.MANY)\n" +
                "                    .withRightSideObjectName(\"beispielBs\")\n" +
                "                    .withMetaTags(AutomatonRelation.MANY_TO_MANY)\n" +
                "            )\n" +
                "            .withRelation(\n" +
                "                new RelationBuilder()\n" +
                "                    .withPojoFields(IBeispielLink.class, Collections.singletonList(\"beispielBId\")," +
                " BeispielB.class, Collections.singletonList(\"id\"))\n" +
                "                    .withSourceField(SourceField.OBJECT_AND_SCALAR)\n" +
                "                    .withLeftSideObjectName(\"beispielB\")\n" +
                "                    .withTargetField(TargetField.MANY)\n" +
                "                    .withRightSideObjectName(\"beispielAs\")\n" +
                "                    .withMetaTags(AutomatonRelation.MANY_TO_MANY)\n" +
                "            )\n" +
                "\n" +
                "\n" +
                "            // configure name fields\n" +
                "            .configureNameField(\"name\")\n" +
                "\n" +
                "            .configureNameFields(Bar.class, \"name\", \"description\")\n" +
                "            .configureNameFields(Qux.class, \"asName\")\n" +
                "\n" +
                "\n" +
                "            /*\n" +
                "                documentation for the types defined in the automaton library\n" +
                "             */\n" +
                "            .withTypeDocsFrom(\n" +
                "                new ClassPathResource(\"automaton-typedocs.json\").getInputStream()\n" +
                "            )\n" +
                "            /*\n" +
                "                hand-written JSON docs for example\n" +
                "             */\n" +
                "            .withTypeDocsFrom(\n" +
                "                new ClassPathResource(\"domain-typedocs.json\").getInputStream()\n" +
                "            )\n" +
                "            /*\n" +
                "                local source docs (autogenerated by the maven 'update-typedocs' execution)\n" +
                "             */\n" +
                "            .withTypeDocsFrom(\n" +
                "                new ClassPathResource(\"source-typedocs.json\").getInputStream()\n" +
                "            )\n" +
                "            .build();\n" +
                "\n" +
                "        return domainQL;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Bean\n" +
                "    public GraphQL graphQL(DomainQL domainQL)\n" +
                "    {\n" +
                "        return GraphQL.newGraphQL(\n" +
                "            domainQL.getGraphQLSchema()\n" +
                "        ).build();\n" +
                "    }\n" +
                "}\n")
        );

    }
}
