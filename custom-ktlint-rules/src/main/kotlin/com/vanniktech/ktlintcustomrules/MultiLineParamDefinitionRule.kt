package com.vanniktech.ktlintcustomrules

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.psi.psiUtil.children
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

class MultiLineParamDefinitionRule : Rule("multi-line-params") {
  override fun visit(
      node: ASTNode,
      autoCorrect: Boolean,
      emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
  ) {
    if (node.elementType == KtStubElementTypes.VALUE_PARAMETER_LIST) {
      val numParameters = node.children().count { it.elementType == KtStubElementTypes.VALUE_PARAMETER }
      if (numParameters > 1 && node.text.count { it == '\n' } != numParameters - 1 ) {
        emit(node.startOffset, "Multiple params on the same line", true)
        if (autoCorrect) {
          val whitespaceChildren = node.children().filter { it is PsiWhiteSpaceImpl }.toList()
          for (child in whitespaceChildren) {
            node.removeChild(child)
          }
          val baseOffset = node.treeParent.treePrev.text.trim('\n')
          val parameters = node.children().filter { it.elementType == KtStubElementTypes.VALUE_PARAMETER }
          for (parameter in parameters) {
            node.addChild(PsiWhiteSpaceImpl("\n    $baseOffset"), parameter)
          }
          node.addChild(PsiWhiteSpaceImpl("\n$baseOffset"), node.lastChildNode)
        }
      }
    }
  }
}
