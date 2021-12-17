<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if post_id?has_content>
        <#assign pId = c.findBy(posts, "id", post_id)!/>
        <@c.postPage pId false/>
        <#else>
        no such post
    </#if>
</@c.page>
