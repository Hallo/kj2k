package org.jetbrains.jet.j2k.ast

import org.jetbrains.jet.j2k.ast.types.Type
import org.jetbrains.jet.j2k.util.AstUtil
import java.util.List

public open class MethodCallExpression(val methodCall: Expression,
                                       val arguments: List<Expression>,
                                       val typeParameters: List<Type>,
                                       val resultIsNullable: Boolean = false): Expression() {
    public override fun isNullable(): Boolean = methodCall.isNullable() || resultIsNullable

    public override fun toKotlin(): String {
        val typeParamsToKotlin: String? = (if (typeParameters.size() > 0)
            "<" + AstUtil.joinNodes(typeParameters, ", ") + ">"
        else
            "")
        val argumentsMapped = arguments.map { it.toKotlin() }
        return methodCall.toKotlin() + typeParamsToKotlin + "(" + argumentsMapped.makeString(", ") + ")"
    }
}
