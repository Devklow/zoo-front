window.addEventListener("load", ()=>{
    document.body.addEventListener("mousemove", (e)=>{
        coordinate(e)
    })
})


function coordinate(event) {
    let x = event.clientX-50;
    let y = event.clientY-50;
    document.getElementById("coordX").innerText = x;
    document.getElementById("coordY").innerText = y;
}