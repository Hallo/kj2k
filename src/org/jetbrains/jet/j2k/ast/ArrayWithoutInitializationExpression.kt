package org.jetbrains.jet.j2k.ast

import org.jetbrains.jet.j2k.util.AstUtil
import org.jetbrains.jet.j2k.ast.types.ArrayType
import java.util.List
import org.jetbrains.jet.j2k.ast.types.Type

public open class ArrayWithoutInitializationExpression(val `type` : Type, val expressions : List<Expression>) : Expression() {
    public override fun toKotlin() : String {
        if (`type`.getKind() == INode.Kind.ARRAY_TYPE)
        {
            return constructInnerType(`type` as ArrayType, expressions)
        }

        return getConstructorName(`type`)
    }

    private fun constructInnerType(hostType : ArrayType, expressions: List<Expression>) : String {
        if (expressions.size() == 1)
        {
            return oneDim(hostType, expressions[0])
        }

        val innerType = hostType.elementType
        if (expressions.size() > 1 && innerType.getKind() == INode.Kind.ARRAY_TYPE)
        {
            return oneDim(hostType, expressions[0], "{" + constructInnerType(innerType as ArrayType, expressions.subList(1, expressions.size())) + "}")
        }

        return getConstructorName(hostType)
    }

    class object {
        private open fun oneDim(`type` : Type, size : Expression) : String {
            return oneDim(`type`, size, "")
        }

        private open fun oneDim(`type` : Type, size : Expression, init : String) : String {
            val commaWithInit = (if (init.isEmpty())
                ""
            else
                ", " + init)
            return getConstructorName(`type`) + "(" + size.toKotlin() + commaWithInit + ")"
        }

        private open fun getConstructorName(`type` : Type) : String {
            return AstUtil.replaceLastQuest(`type`.toKotlin())
        }
    }
}
