<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if (user_id?has_content) && (c.findBy(users, "id", user_id)?has_content)>
        <div>посты пользователя с id=${user_id}</div>
        <#assign pIdlist = c.filter(posts, "user_id", user_id)/>
        <#list pIdlist as p>
            <div>-> <@c.postPage p true/></div>
        </#list>
        <#else>
        no such user
    </#if>
</@c.page>
