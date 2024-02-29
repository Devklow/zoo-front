@startuml
partition vue{
(*) -->index.jsp
--> InitServlet

}
@enduml

@startuml
|Vue|
start
:index.jsp;
->**[Forwarding]**;
|#Lavender|Controleur|
if (InitServlet) is (**[listPOJO]**) then
   |Vue|
   :principale.jsp;
   stop
else (**[getCagesPOJOs]**)
   |Metier|
   :Manageur;
detach
endif
@enduml
@startuml
|Vue|
start
:principale.jsp;
->**[GET() (click.)]**;
|#Lavender|Controleur|
if (NourrirServlet) is (**2° [Forwarding] **) then
   |Vue|
   :InitServlet;
   stop
else (**1° [nourrir()]**)
   |Metier|
   :Manageur;
detach
endif
@enduml