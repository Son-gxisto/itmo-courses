<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
<#if handle?has_content>
    <#assign uId = c.findBy(users, "handle", handle)!/>
    <@c.userPage uId/>
    <#else>
        <#if user_id?has_content>
            <#assign uId = c.findBy(users, "id", user_id)!/>
            <@c.userPage uId/>
            <#else>
            no such user
        </#if>
</#if>
</@c.page>
