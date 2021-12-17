window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
};
function ajax(dataVar, successFunc, $error) {
    $.ajax({
        type: "POST",
        url: "",
        dataType: "json",
        data: dataVar,
        success: function (response) {
            if (successFunc !== undefined) {
                successFunc(response);
                return;
            }
            if (response["error"]) {
                $error.text(response["error"]);
            } else {
                if (response["redirect"]) {
                    location.href = response["redirect"];
                }
            }
        }
    });
}
/*
                        $art.find("button").click(function() {
                            ajax({
                                action: "hos"
                            },
                            function(response) {
                                article["isHidden"] = !article["isHidden"];
                                if (article["isHidden"]) {
                                    $art.find("button").text("show");
                                } else {
                                    $art.find("button").text("hide");
                                }
                            })
                        });
 */