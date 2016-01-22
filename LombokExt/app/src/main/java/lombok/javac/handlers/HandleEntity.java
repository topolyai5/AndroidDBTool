package lombok.javac.handlers;


import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import org.mangosdk.spi.ProviderFor;

import java.lang.reflect.Modifier;
import java.util.Collection;

import lombok.Repository;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import static lombok.javac.handlers.JavacHandlerUtil.deleteAnnotationIfNeccessary;
import static lombok.javac.handlers.JavacHandlerUtil.namePlusTypeParamsToTypeReference;


@ProviderFor(JavacAnnotationHandler.class)
public class HandleEntity extends JavacAnnotationHandler<Repository> {
    @Override
    public void handle(AnnotationValues<Repository> annotation, JCAnnotation ast, JavacNode annotationNode) {
        System.err.println("This is ERROR...");
        deleteAnnotationIfNeccessary(annotationNode, Repository.class);
        JCMethodDecl contentValues = null;
        try {
            contentValues = createContentValues(annotation, ast, annotationNode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        p("meth def " + contentValues);
        JavacHandlerUtil.injectMethod(annotationNode.up(), contentValues);
    }

    /*@Override
    public ContentValues createContentValues(TestDomain entity) {
        return null;
    }*/
    private JCMethodDecl createContentValues(AnnotationValues<Repository> annotation, JCAnnotation ast, JavacNode annotationNode) throws ClassNotFoundException {

        System.err.println("Start... ");
        Object t = annotation.getActualExpression("type");
        Class clss = Class.forName(((JCTree.JCFieldAccess) t).selected.type.toString());
        System.err.println("Clss: " + clss.getName());

        Collection<JavacNode> fields = annotationNode.upFromAnnotationToFields();
        if (fields == null || fields.isEmpty()) {
            System.err.println("Fields are empty.");

        } else {
            for (JavacNode field : fields) {
                System.err.println("Field: " + field.getName());

            }
        }

        JavacNode node = annotationNode.up();
        if (node == null) {
            p("Node is null");
        } else {
            JavacTreeMaker treeMaker = node.getTreeMaker();
            JCClassDecl decl = (JCClassDecl) annotationNode.up().get();
            JCExpression methodType = namePlusTypeParamsToTypeReference(treeMaker, node.toName("ContentValues"), decl.getTypeParameters());
            p("method type is setted  " + decl);
            JCModifiers modifiers = treeMaker.Modifiers(Modifier.PUBLIC);
            Name methodName = node.toName("createContentValues");
            List<JCTypeParameter> methodGenericTypes = List.nil();
            JCExpression paramvarType = namePlusTypeParamsToTypeReference(treeMaker, node.toName(clss.getSimpleName()), decl.getTypeParameters());
            JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), node.toName("entity"), paramvarType, null);
            List<JCVariableDecl> methodParameters = List.of(param);

            List<JCExpression> methodThrows = List.nil();

            JCTree.JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(node.toName("null")));
            JCBlock methodBody = treeMaker.Block(0, List.<JCStatement>of(returnStatement));


            return treeMaker.MethodDef(
                    modifiers,
                    methodName,
                    methodType,
                    methodGenericTypes,
                    methodParameters,
                    methodThrows,
                    methodBody,
                    null
            );
        }

        p("Hello world");
        return hw(annotationNode.up());
    }

    private JCMethodDecl hw(JavacNode type) {
        JavacTreeMaker treeMaker = type.getTreeMaker();

        JCModifiers modifiers = treeMaker.Modifiers(Modifier.PUBLIC);
        List<JCTypeParameter> methodGenericTypes = List.<JCTypeParameter>nil();
//        JCExpression          methodType         = treeMaker.TypeIdent(TypeTags.VOID);
        JCExpression methodType = treeMaker.getUnderlyingTreeMaker().TypeIdent(TypeTags.VOID);
        Name methodName = type.toName("helloWorld");
        List<JCVariableDecl> methodParameters = List.<JCVariableDecl>nil();
        List<JCExpression> methodThrows = List.<JCExpression>nil();

        JCExpression printlnMethod = JavacHandlerUtil.chainDots(type, "System", "out", "println");
        List<JCExpression> printlnArgs = List.<JCExpression>of(treeMaker.Literal("hello world"));
        JCMethodInvocation printlnInvocation = treeMaker.Apply(List.<JCExpression>nil(), printlnMethod, printlnArgs);
        JCBlock methodBody = treeMaker.Block(0, List.<JCStatement>of(treeMaker.Exec(printlnInvocation)));

        JCExpression defaultValue = null;

        return treeMaker.MethodDef(
                modifiers,
                methodName,
                methodType,
                methodGenericTypes,
                methodParameters,
                methodThrows,
                methodBody,
                defaultValue
        );
    }

    private void p(String msg) {
        System.err.println(msg);
    }

}
