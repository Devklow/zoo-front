<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="fr">
    <head>
		<link rel="icon" type="image/png" href="images/logo.png" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>zoo en folie</title>
        <link rel="stylesheet" type="text/css" href="zoo.css"/>
        <link rel="stylesheet" type="text/css" href="info-bulle.css"/>
        <link rel="stylesheet" type="text/css" href="bootstrap.min.css"/>
	</head>
	<body class="bg-dark">
<header>
</header>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-warning sticky-top">
	<a class="navbar-brand" href="#">
		<img class="d-inline-block align-top ms-2" alt="" src="images/logo.png" width="50" height="50"/>
	</a>
	<div class="collapse navbar-collapse" id="navbarNav">
		<ul class="navbar-nav d-flex w-100 justify-content-around">
			<li class="nav-item active">
				<a class="nav-link" href="nourrir">Nourrir les animaux</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#" onclick="document.fzoo.submit();">Devorer</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="entrerVisiteur">Faire entrer un visiteur</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="sortirVisiteur">Faire sortir un visiteur</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#ajouterCage">Ajouter une cage</a>
			</li>
		</ul>
	</div>
</nav>
<article>
	<img alt="mon zoo" src="images/zooparc3.jpg" width="100%" height="100%"/>
	<form name="fzoo" action="devorer" method="post">
		<input type="hidden" value="devorer" name="action"/>
		<c:forEach items="${cages}" var="cage">
			<div style="position:absolute;top:${cage.getY()}px;left:${cage.getX()}px">
				<img data-id="${cage.getId()}" src="${cage.getImage()}" class="animal"/>
				<div id="pancarte-${cage.getId()}" class="afficheAnimal d-none" >${cage.getPancarte()}<br/>
                    <c:if test = "${cage.isVide() == true}">
						<a class="btn btn-sm btn-info btn-sml ms-1 me-1 addAnimal" data-bs-toggle="modal"
						   data-bs-target="#ajouterAnimal" type="button" data-id="${cage.getId()}">Ajouter un animal</a>
                    </c:if>
                    <c:if test = "${cage.isVide() == false}">
                        <input type="radio" name="mangeur" value="${cage.getId()}"> Mangeur &nbsp;&nbsp;
                        <input type="radio" name="mange" value="${cage.getId()}"> Mange<br>
                    </c:if>
				</div>
			</div>
		</c:forEach>
		<div id="visiteurs" class="position-fixed bottom-0 start-0 mb-4">
            <c:forEach begin="1" end="${visiteurs}">
				<img class="visiteur" src="images/visiteur.png"/>
			</c:forEach>
            <c:if test="${visiteurs>0}">
                <input id="visiteurRadio" type="radio" name="mange" value="visiteur">
                <label for="visiteurRadio" class="text-white">
                    Mange
                </label>
            </c:if>
        </div>
	</form>
</article>
        <c:forEach items="${notifs}" var="notif">
            <div id="info-bulle-container" class="info-bulle-top-right">
                <div class="info-bulle info-bulle-${notif.getType()}" aria-live="polite" style="">
                    <div class="info-bulle-message">${notif.getMessage()}
                    </div>
                </div>
            </div>
        </c:forEach>
<script src="info-bulle.js" defer></script>
<script src="coord.js" defer></script>
<script src="principale.js" defer></script>
<script src="bootstrap.bundle.min.js">
</script>

<!-- Modal -->
<div class="modal fade" id="ajouterCage" tabindex="-1" aria-labelledby="ajouterCageLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="ajouterCageLabel">Ajouter une cage</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
			</div>
			<div class="modal-body">
				<form name="ajouterCageForm" id="ajouterCageForm" action="ajouterCage">
					<div class="col-auto mb-3 fw-bold">
						Coordonnées :
					</div>
					<div class="row">
						<div class="col-2 d-flex justify-content-end">
							<label for="x" class="col-form-label required">X :</label>
						</div>
						<div class="col-3">
							<input type="text" name="x" id="x" class="form-control" required="required">
						</div>
						<div class="col-2 d-flex justify-content-end">
							<label for="y" class="col-form-label required">Y :</label>
						</div>
						<div class="col-3">
							<input type="text" name="y" id="y" class="form-control" required="required">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer d-flex justify-content-center">
				<button type="button" class="btn btn-danger me-3" data-bs-dismiss="modal">Annuler</button>
				<button type="button" class="btn btn-primary" onclick="document.ajouterCageForm.submit()">Ajouter</button>
			</div>
		</div>
	</div>
</div>


<div class="modal fade" id="ajouterAnimal" tabindex="-1" aria-labelledby="ajouterAnimalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="ajouterAnimalLabel">Ajouter un animal</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
			</div>
			<div class="modal-body">
				<form name="ajouterAnimalForm" id="ajouterAnimalForm" action="ajouterAnimal">
					<label for="addAnimalClasse" class="form-label">Type de l'animal :</label>
					<input type="hidden" name="addAnimalCageId" value="X" id="addAnimalCageId">
						<select id="addAnimalClasse" name="addAnimalClasse" class="form-select" aria-label="types animal">
							<c:forEach items="${classes}" var="classe">
								<option value="${classe}">${classe}</option>
							</c:forEach>
						</select>
                    <label for="addAnimalName" class="form-label">Nom :</label>
                    <input type="text" class="form-control" name="addAnimalName" id="addAnimalName">
                    <label for="addAnimalAge" class="form-label">Age :</label>
                    <input type="text" class="form-control" name="addAnimalAge" id="addAnimalAge">
                    <label for="addAnimalWeight" class="form-label">Poids :</label>
                    <input type="text" class="form-control" name="addAnimalWeight" id="addAnimalWeight">
				</form>
			</div>
			<div class="modal-footer d-flex justify-content-center">
				<button type="button" class="btn btn-danger me-3" data-bs-dismiss="modal">Annuler</button>
				<button type="button" class="btn btn-primary" onclick="document.ajouterAnimalForm.submit()">Ajouter</button>
			</div>
		</div>
	</div>
</div>

	<div class="position-fixed bottom-0 start-0 text-white p-0 m-0 w-100 bg-dark">
		<div class="row">
			<div class="col-auto fw-bold me-1">
				Coordonées&nbsp;:
			</div>
			<div class="col-auto p-0" id="coordX">
				x
			</div>
			<div class="col-auto fw-bold">
				x
			</div>
			<div class="col-auto p-0" id="coordY">
				y
			</div>
			<div class="col-auto fw-bold">
				y
			</div>
		</div>
	</div>
</body>
</html>
