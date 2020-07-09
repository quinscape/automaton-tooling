package $BASE_PACKAGE.runtime.config;

import de.quinscape.automaton.runtime.scalar.ConditionScalar;
import de.quinscape.automaton.runtime.scalar.ConditionType;
import de.quinscape.automaton.runtime.scalar.FieldExpressionScalar;
import de.quinscape.automaton.runtime.scalar.FieldExpressionType;

import $BASE_PACKAGE.domain.Public;
$POJO_IMPORTS
import de.quinscape.domainql.DomainQL;
import de.quinscape.domainql.RelationBuilder;
import de.quinscape.domainql.annotation.GraphQLLogic;
import de.quinscape.domainql.config.SourceField;
import de.quinscape.domainql.config.TargetField;
import de.quinscape.domainql.generic.DomainObject;
import de.quinscape.domainql.generic.DomainObjectScalar;
import de.quinscape.domainql.generic.GenericScalar;
import de.quinscape.domainql.generic.GenericScalarType;
import de.quinscape.domainql.jsonb.JSONB;
import de.quinscape.domainql.jsonb.JSONBScalar;
import graphql.GraphQL;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;


import static $BASE_PACKAGE.domain.Tables.*;

/**
 * $DESCRIPTION
 */
@Configuration
public class GraphQLConfiguration
{
    private final static Logger log = LoggerFactory.getLogger(GraphQLConfiguration.class);

    private final ApplicationContext applicationContext;

    private final DSLContext dslContext;


    @Autowired
    public GraphQLConfiguration(
        ApplicationContext applicationContext,
        DSLContext dslContext
    )
    {
        this.applicationContext = applicationContext;
        this.dslContext = dslContext;
    }

    
    @Bean
    public DomainQL domainQL() throws IOException
    {
        final Collection<Object> logicBeans = applicationContext.getBeansWithAnnotation(GraphQLLogic.class).values();

        final DomainQL domainQL = DomainQL.newDomainQL(dslContext)

            .logicBeans(logicBeans)

            .objectTypes(Public.PUBLIC)

            .withAdditionalScalar(DomainObject.class, DomainObjectScalar.newDomainObjectScalar())
            .withAdditionalScalar(JSONB.class, new JSONBScalar())
            .withAdditionalScalar(ConditionScalar.class, ConditionType.newConditionType())
            .withAdditionalScalar(FieldExpressionScalar.class, FieldExpressionType.newFieldExpressionType())
            .withAdditionalScalar(GenericScalar.class, GenericScalarType.newGenericScalar())

            .withAdditionalInputTypes(
                $INPUT_TYPES
            )
            
            // configure object creation for foreign key relationships
            $FOREIGN_KEY_RELATIONS
            // configure object creation for view / POJO-based relationships
            $VIEW_RELATIONS

            // configure name fields
            $NAME_FIELDS
            $NAME_FIELDS_BY_TYPE

            /*
                documentation for the types defined in the automaton library
             */
            .withTypeDocsFrom(
                new ClassPathResource("automaton-typedocs.json").getInputStream()
            )
            /*
                hand-written JSON docs for example
             */
            .withTypeDocsFrom(
                new ClassPathResource("domain-typedocs.json").getInputStream()
            )
            /*
                local source docs (autogenerated by the maven 'update-typedocs' execution)
             */
            .withTypeDocsFrom(
                new ClassPathResource("source-typedocs.json").getInputStream()
            )
            .build();

        return domainQL;
    }


    @Bean
    public GraphQL graphQL(DomainQL domainQL)
    {
        return GraphQL.newGraphQL(
            domainQL.getGraphQLSchema()
        ).build();
    }
}