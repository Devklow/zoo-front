document.querySelectorAll(".addAnimal").forEach((el)=>{
    el.addEventListener("click", (ev)=>{
        let id = el.dataset.id
        document.getElementById("addAnimalCageId").value = id;
    })
})

document.getElementById("addAnimalClasse").addEventListener("change", ()=>{
    let animal = document.getElementById("addAnimalClasse").value
    console.log(animal)
    if(animal==="Gazelle"){
        let input = document.createElement("input")
        input.setAttribute("id","addAnimalLgCornes")
        input.setAttribute("name","addAnimalLgCornes")
        input.setAttribute("type","text")
        input.setAttribute("class","form-control")
        let label = document.createElement("label")
        label.setAttribute("id", "addAnimalLgCornesLabel")
        label.setAttribute("for", "addAnimalLgCornes")
        label.setAttribute("class", "form-label")
        label.innerText = "Longueur des cornes :"
        document.getElementById("ajouterAnimalForm").appendChild(label);
        document.getElementById("ajouterAnimalForm").appendChild(input);
    } else {
        document.getElementById("addAnimalLgCornesLabel").remove()
        document.getElementById("addAnimalLgCornes").remove()
    }
})


document.querySelectorAll('.animal').forEach((el)=>{
    el.addEventListener("click", ()=>{
        document.getElementById("pancarte-"+el.dataset.id).classList.toggle("d-none");
    })
})