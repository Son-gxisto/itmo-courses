let cards = document.getElementsByClassName("pigItem");
let buttons = document.getElementsByClassName("tiButton");
let cur = 0;
function nextCard() {
  if (cur !== cards.length - 1) {
    let curCstyle = cards[cur].style;
    let nextCstyle = cards[cur + 1].style;
    cur++;
    curCstyle.opacity = "0";
    setTimeout(function() {
      curCstyle.display = "none";
      nextCstyle.display = "block";
      nextCstyle.opacity = "1";
    }, 500)
  }
}
function funload() {
  for (let i = 0; i < buttons.length; i++) {
    let curStyle = buttons[i].children[0].style;
    buttons[i].addEventListener("click", function () {
      nextCard();
      curStyle.width = "70px";
      curStyle.height = "70px";
      curStyle.left = "0";
      curStyle.top = "0";
      setTimeout(function () {
        curStyle.width = "60px";
        curStyle.height = "60px";
        curStyle.left="5px";
        curStyle.top = "5px";
      }, 300)
    })
  }
}
