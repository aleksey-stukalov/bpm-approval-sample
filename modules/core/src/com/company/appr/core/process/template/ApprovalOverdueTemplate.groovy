package com.company.appr.core.process.template

title = "The aproval \"${entity.getInstanceName()}\" is overdue"

body = """Hi ${user.name},
<p>
The approval is overdue: \"${entity.getInstanceName()}\"
</p>
<p>
<p>
The due date was: ${entity.getDueDate().format('dd-MM-yyyy HH:mm')}
</p>
<a href='${editorLink}'>Open the approval</a>
</p>
"""
