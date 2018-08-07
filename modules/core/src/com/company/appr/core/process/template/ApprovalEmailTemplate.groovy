package com.company.appr.core.process.template

title = "Your approval is required for \"${entity.getInstanceName()}\""

body = """Hi ${user.name},
<p>
You approval is required: ${entity.getInstanceName()}.
</p>
<p>
<a href='${editorLink}'>Open the approval</a>
</p>
"""
