<template>
  <textarea ref="code-container" v-bind:value="value"></textarea>
</template>

<script>
import CodeMirror from 'codemirror'
import './formatting'
import 'codemirror/mode/xml/xml.js'
import 'codemirror/addon/hint/xml-hint.js'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/material.css'

let editor = CodeMirror.EditorFromTextArea

function autoFormatSelection() {
  // 选中所有行
  CodeMirror.commands['selectAll'](editor)
  // 获取被选中的开始行号和结束行号
  let range = {
    from: editor.getCursor(true),
    to: editor.getCursor(false)
  }
  // 基于选中的行号进行格式化
  editor.autoFormatRange(range.from, range.to)
  // 取消选择, 并去到第一行
  CodeMirror.commands["goDocStart"](editor)
}

export default {
  name: 'XmlCodeMirror',
  props: {
    value: {
      type: String,
      require: true
    },
    readOnly: {
      type: Boolean,
      default: true
    }
  },
  mounted() {
    if (!editor) {
      editor = CodeMirror.fromTextArea(
        this.$refs['code-container'],
        {
          lineWrapping: true,
          mode: 'application/xml', // HMTL混合模式
          lineNumbers: true, // 是否显示行号
          lint: true, // 是否高亮显示代码
          theme: 'material', // 主题，需要配合上方 import 的 css
          readOnly: this.readOnly // 是否只读
        }
      )
      editor.setSize('100%', '100%')
    } else {
      editor.setValue(this.value)
    }
    autoFormatSelection()
  },
  watch: {
    value: (val, oldVal) => {
      editor.setValue(val)
      autoFormatSelection()
    }
  }
}
</script>
