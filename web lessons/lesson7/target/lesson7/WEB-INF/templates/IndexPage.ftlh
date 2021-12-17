<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    Welcome on Codeforces!
    <div class="articles">
        <div class="noData">No articles</div>
        <template>
            <div class="article">
                <div class="article_title"></div>
                <div class="article_text"></div>
            </div>
        </template>
    </div>

<script>
    $(function () {
        $("aside section").click(function () {
            $(this).css("background-color", "red");
        });
    })
</script>
<script>
    $(function () {
        const $noData = $(".articles .noData");
        ajax({
                action: "findAll"
            },
            function(response) {
                if (response["articles"].size > 0) {$noData.text("Loading...");}
                response["articles"].forEach(function(article) {
                    if (!article["hidden"]) {
                        $noData.hide();
                        const $art = $($(".articles template").prop("content")).clone();
                        $art.find(".article_title").text(article["title"]);
                        $art.find(".article_text").text(article["text"]);
                        $(".articles").append($art);
                    }
                });
            }
        );
    })
</script>
</@c.page>
