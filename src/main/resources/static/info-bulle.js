function fadeOutEffect(el, speed) {
    let fadeEffect = setInterval(function () {
        if (!el.style.opacity) {
            el.style.opacity = 1;
        }
        if (el.style.opacity > 0) {
            el.style.opacity -= speed;
        } else {
            el.remove()
            clearInterval(fadeEffect);
        }
    }, 50);
}

document.querySelectorAll(".info-bulle").forEach((el)=>
{
    el.addEventListener('mouseout', ()=>{
        fadeOutEffect(el, 0.05);
    });
    el.addEventListener('click', ()=>{
        fadeOutEffect(el, 0.1);
    });
})