<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-vs-coronavirus-65.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"},
            {"href": "/misc/users", "name": "Users"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if uri?? && uri?starts_with(item.href)>
                            <a style="text-decoration: underline; text-decoration-color: blue" href="${item.href}">${item.name}</a>
                        <#else>
                            <a href="${item.href}">${item.name} </a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#if posts?has_content>
        <#list posts as p>
            <section>
                <div class="header">Post №${p.id}</div>
                <div class="body"> <@postPage p_index true/>
                </div>
                <div class="footer">
                    <a href="/post?post_id=${p.id}">View all</a>
                </div>
            </section>
        </#list>
        </#if>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2020 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly>
    <#if nameOnly>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    <#else>
        <#assign map={"RED" : "red", "BLUE" : "blue", "GREEN" : "green" }>
        <a style="color:${map[user.color]}; text-decoration: none" href="/user?handle=${user.handle}">${user.handle}</a>
    </#if>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item_index/>
        </#if>
    </#list>
</#function>

<#function filter items name value>
    <#local result = []>
    <#list items as item>
        <#if item[name] == value>
            <#local result = result + [item?index]>
        </#if>
    </#list>
    <#return result>
</#function>

<#macro userPage uId>
    <#if (uId?has_content) && (users[uId]?has_content)>
        <#assign user = users[uId]/>
        <div class="userContent">
            <div class="textContent">
                <h1> <@userlink user false/></h1>
                <ul>
                    <li>id: ${user.id}</li>
                    <li>handle: ${user.handle}</li>
                    <li>name: ${user.name}</li>
                    <li><a href="/posts?user_id=${user.id}">posts</a></li>
                </ul>
            </div>
            <img title="user picture" src="/img/user-picture.png" alt="user picture"/>
        </div>
        <div class="userChange">
        <#if uId == 0>
            <a style="color:darkgray; text-decoration: none">←</a>
        <#else>
            <a style="color:blue; text-decoration: none" href="/user?handle=${users[uId - 1].handle}">←</a>
        </#if>
        <#if uId + 1 == users?size>
            <a style="color:darkgray; text-decoration: none">→</a>
        <#else>
            <a style="color:blue; text-decoration: none" href="/user?handle=${users[uId + 1].handle}">→</a>
        </#if>

        </div>
    <#else>
        No such user
    </#if>
</#macro>

<#macro postPage pId shortForm>
    <div>
    <#if (pId?has_content) && (posts[pId]?has_content)>
        <#if (posts[pId].text?length >= 250) && (shortForm)>
            ${posts[pId].text[0..<250]}...
        <#else>
            ${posts[pId].text}
        </#if>
    <#else>
        no such page
    </#if>
    </div>
</#macro>
<#macro userTable userList>
    <div class="userTable"> users
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>UserHandle</th>
                <th>UserName</th>
            </tr>
            </thead>
            <tbody>
            <#list userList as u>
                <tr>
                    <th>${u.id}</th>
                    <th>${u.handle}</th>
                    <th><@userlink u false/></th>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>