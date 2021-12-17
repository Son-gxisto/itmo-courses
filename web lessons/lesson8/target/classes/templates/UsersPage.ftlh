<#-- @ftlvariable name="users" type="ru.itmo.wp.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
            </tr>
            </thead>
            <tbody>
            <#if users?size==0>
                <tr class="noData">
                    <td colspan="3">
                        No data
                    </td>
                </tr>
            <#else>
                <#list users as user>
                <tr>
                    <td class="id">${user.id}</td>
                    <td class="login">${user.login}</td>
                    <td class="creationTime">${user.creationTime}</td>
                    <td class="setDisabled">
                        <form method="post" action="">
                        <input type="hidden" name="action" value="setDisabled"/>
                        <input type="hidden" name="id" value="${user.id}"/>
                        <#if user.disabled>
                            <#assign t="Enable"/>
                        <#else>
                            <#assign t="Disable"/>
                        </#if>
                        <input type="hidden" name="disabled" value="${t}"/>
                        <input type="submit" value="${t}">
                    </form>
                    </td>

                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>
