package tenant.test;

import grails.gorm.MultiTenant;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.grails.datastore.mapping.model.config.GormProperties;
import org.grails.datastore.mapping.reflect.AstUtils;

import java.lang.reflect.Modifier;

/**
 * Add Long tenantId property and the MultiTenant trait to domain classes.
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class TenantEntityTransformation implements ASTTransformation {

    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (null == nodes) return;
        if (null == nodes[0]) return;
        if (null == nodes[1]) return;
        if (!(nodes[0] instanceof AnnotationNode)) return;

        ClassNode cNode = (ClassNode) nodes[1];

        injectTenantIdProperty(cNode);
        addMultiTenantTrait(cNode);
    }

    private void injectTenantIdProperty(ClassNode classNode) {
        final boolean hasId = AstUtils.hasOrInheritsProperty(classNode, GormProperties.TENANT_IDENTITY);

        if (!hasId) {
            // inject into furthest relative
            ClassNode parent = AstUtils.getFurthestUnresolvedParent(classNode);

            parent.addProperty(GormProperties.TENANT_IDENTITY, Modifier.PUBLIC, new ClassNode(Long.class), null, null, null);
        }
    }

    private void addMultiTenantTrait(ClassNode cNode) {
        AstUtils.injectTrait(cNode, MultiTenant.class);
    }
}
