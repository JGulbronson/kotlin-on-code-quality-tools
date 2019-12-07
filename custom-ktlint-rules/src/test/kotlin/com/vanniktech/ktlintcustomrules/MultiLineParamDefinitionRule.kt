package com.vanniktech.ktlintcustomrules

import com.github.shyiko.ktlint.test.format
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MultiLineParamDefinitionRuleTest {
  @Test
  fun multiLineParams() {
    assertThat(MultiLineParamDefinitionRule().format("""
        class Foo {
          fun test(a: Int, b: String,
              c: Double) {
            return a * 2
          }
        }
        """.trimIndent()
    )).isEqualTo(
        """
        class Foo {
          fun test(
              a: Int,
              b: String,
              c: Double
          ) {
            return a * 2
          }
        }
        """.trimIndent()
    )
  }
}
