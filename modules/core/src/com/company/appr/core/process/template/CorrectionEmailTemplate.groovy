package com.company.appr.core.process.template

title = "Approval correction is required for \"${entity.getInstanceName()}\""

body = """Hi ${user.name},
<p>
The approval must be corrected: \"${entity.getInstanceName()}\"
</p>
<p>
<a href='${editorLink}'>Open the approval</a>
</p>
"""
