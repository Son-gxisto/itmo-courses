<#import "./common.ftlh" as c>

<@c.page>
    <#list posts as p>
        ${p.id}: <@c.postPage p_index true/>
    </#list>
</@c.page>