<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th class="admin">Admin</th>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user_id"></td>
                    <td class="user_login"></td>
                    <td class="user_admin"><p></p><a href="#"></a></td>
                </tr>
            </template>
            <tr class="noData">
                <td colspan="3">
                    No data
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");
            $noData.find("td").text("Loading...");
            ajax({
                action: "findAll"
            },
                function(response) {
                    response["users"].forEach(function(user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user_id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        $tr.find("tr").attr("data-userId", user["id"]);
                        $tr.find(".user_admin p").text(user["admin"]);
                        if (user["admin"]) {
                            $tr.find(".user_admin a").text("disable");
                        } else {
                            $tr.find(".user_admin a").text("enable");
                        }
                        $(".users tbody").append($tr);
                    });
                    $(".users tr").click(function() {
                        $.ajax({
                            dataType: "json",
                            data: {
                                action: "findUser",
                                userId: $(this).attr("data-userId")
                            },
                            success: function (response) {
                                notify(response["user"].creationTime);
                            },
                            type: "POST"
                        })
                    });
                    $(".users a").click(function() {
                        var $a=$(this);
                        ajax({
                            action: "setAdmin",
                            admin_id: $a.parent().parent().attr("data-userId"),
                            value: $a.attr("text")
                            },
                            function(response) {
                                var $p=$a.parent().children("p");
                                if ($a.attr("text") === "enable") {
                                    $p.text("true");
                                    $a.text("disable");
                                } else {
                                    $p.text("false");
                                    $a.text("enable");
                                }
                            }
                        )
                    });
                    const admin = response["user"];
                    if (!(admin !== undefined && admin["admin"])) {
                        $(".users .user_admin").hide();
                        $(".users .admin").hide();
                    }
                }
            );
        })
    </script>
</@c.page>
