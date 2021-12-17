<#import "commons.ftlh" as c>
<@c.page>
    <#if vPost??>
    <@c.post post=vPost></@c.post>
        <#if user??>
            <div class = "add_comment">
                <form method="post" action="">
                    <input type="hidden" name="action" value="addComment"/>
                    <div class="comment">
                        <div class="title">
                            <label for="text">Comment:</label>
                        </div>
                        <div class="value">
                            <textarea id="text" name="text"></textarea>
                        </div>
                    </div>
                    <input type="submit" value="Add comment"/>
                </form>
            </div>
        </#if>
        <div> Comments: </div>
    <#list vPost.comments as comment>
        <@c.comment comment></@c.comment>
    </#list>
        <#else>
        No such post
    </#if>
</@c.page>