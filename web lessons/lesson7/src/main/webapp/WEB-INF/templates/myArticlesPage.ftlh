<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles datatable">
        <#if (articles??) && (articles[0]??)>
        <div class="caption">Articles</div>
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Title</th>
                    <th>Hide</th>
                </tr>
            </thead>
            <tbody>
            <#list articles as article>
                <tr class="article_${article.id}">
                    <td>${article.id}</td>
                    <td>
                        <#if (article.title?length > 50)>
                            ${article.title?substring(0, 50)}...
                        <#else>
                            ${article.title}
                        </#if>
                    </td>
                    <td class="hOs">
                        <#if article.hidden>
                            <#assign val = "Show">
                        <#else>
                            <#assign val = "Hide">
                        </#if>
                        <input class="button" type="submit" id="${article.id}" value="${val}">
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
            <#else>
        <div class="noData">No articles</div>
        </#if>
    </div>
    <script>
        $(function () {
            $(".articles .button").click(
                function (event) {
                    var $button = $(this);
                    $button.attr("disabled", true);
                    ajax({
                            action: "hos",
                            articleId: $button.attr("id"),
                            value: $button.attr("value")
                        },
                        function (response) {
                            if ($button.attr("value") === "Hide") {
                                $button.attr("value", "Show");
                            } else {
                                $button.attr("value", "Hide");
                            }
                            $button.attr("disabled", false);
                        }
                    );
                }
            )
        })
    </script>
</@c.page>