<#-- @ftlvariable name="userP" type="ru.itmo.wp.domain.User" -->
<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>
<@c.page>
    <#if userP??>
        <div class="User">Id: ${userP.id}; Login: ${userP.login}; Creation time: ${userP.creationTime}</div>
        <#else>
        No such user.
    </#if>

</@c.page>
