<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/login.css">
        <title>ilicdoo.com</title>
    </head>

    <body>

        <!-- header-->

        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <nav class="slika">          
                <img src="${pageContext.request.contextPath}/resources/img/ilic.png"/>  
            </nav>
            <div class="container">

                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>




                <div class="collapse navbar-collapse" id="navbarSupportedContent">

                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item active">
                            <a class="nav-link" href="${pageContext.request.contextPath}/">Početna<span class="sr-only">(current)</span></a>
                        </li>
                        <c:if test="${isAdmin}">
                            <li class="nav-item active">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin">Admin <span class="sr-only"></span></a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/onama">O nama</a>
                        </li>
                           <li class="nav-item active"><a href="${pageContext.request.contextPath}/komentari" class="nav-link">Komentari</a>
                              </li>
                       
                        <li class="nav-item">
                            <a class="nav-link" href="#kontakt">Kontakt</a>
                        </li>
                        <c:if test="${email==null}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">Prijava/Registracija</a>
                            </li>
                        </c:if>
                        <c:if test="${email!=null}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    Profil
                                </a>
                                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                    

                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Izloguj se</a>
                                </div>
                            </li>
                        </c:if>
                    </ul>
                </div>

        </nav>

        

        <!-- about --> 
        <section id="about" class="py-5">
            <h2>Komentari naših mušterija:</h2>
            <div class="container">
                <div class="row text-center">
                    <div class="col-md-12">
                           <table class="table table-dark" >
                        <thead>
                            <tr>
                                <th scope="col">Ime i prezime</th>                         
                                <th scope="col">Komentar</th>                                                                                             
                            </tr>
                        </thead>
                        <c:forEach items="${komentari}" var="komentari">
                            <tbody>

                                <tr>
                                    <td> <c:out value="${komentari.ime}"/> <c:out value="${komentari.prezime}"/> </td>
                                    <td><c:out value="${komentari.komentar}"/></td>                                                                                                
                                </tr>

                            </tbody>
                        </c:forEach>
                    </table>
                    </div>
                           <form id="forma" action="${pageContext.request.contextPath}/dodajkomentar" method="post">
                                        

                                        <div class="form-group">
                                            <label for="comment">Dodaj komentar firmi</label>
                                            <textarea class="form-control" rows="1" id="komentar" name="komentar" form="forma" style="margin-left: 50px; max-width:66% "></textarea>
                                        </div> 
                                        <c:if test="${email!=null}">
                                            <button type="submit" class="btn btn-secondary" style="max-width: 60%;">Dodaj  Komentar</button>
                                        </c:if>
                                    </form> 
                </div>
                
                
            </div>
        </section>

      <!-- about --> 
        <section id="about" class="py-5">
            <div class="container">
                <div class="row text-center">
                    <div class="col-md-12">
                      
                    </div>
                </div>
            </div>
        </section>



        <!-- footer -->
        <footer class="py-5" id="kontakt">
            <section class="form py-5">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h2 class="heading">Kontakt</h2>
                            <div class="underline"></div>
                            <p>Ukoliko imate pitanje ili zelite da nam pošaljete neku sugestiju kontaktirajte nas putem mejla! Srdačan pozdrav želi vam tim Nadji Igrača!</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"></div>
                        <div class="col-md-4">
                            <form>
                                <div class="form-group">
                                    <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
                                </div>
                                <div class="form-group">
                                    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                                </div>

                                <div class="form-group">
                                    <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" placeholder="Message"></textarea>
                                </div>
                                <button type="submit" class="btn btn-block">Pošalji</button>
                            </form>
                        </div>
                        <div class="col-md-4"></div>
                    </div>
                </div>
            </section>

          <!-- CONTACT -->
            <section class="contact py-5">
                <div class="container">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <i class="fas fa-map-marker-alt"></i>
                            <h4>Kancelarija</h4>
                            <p>Vlasotince <br> Vlasotince, Srbija</p>
                        </div>
                        <div class="col-md-4 text-center">
                            <i class="fas fa-phone"></i>
                            <h4>Telefon</h4>
                            <p>063 769 6937</p>
                        </div>
                        <div class="col-md-4 text-center">
                            <i class="fas fa-envelope"></i>
                            <h4>Email</h4>
                            <p>ilic@gmail.com <br> ilic@doo.com</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"></div>
                        <div class="col-md-4">
                            <h3 class="text-center">Društvene mreže:</h3>
                            <ul class="list-unstyled d-flex justify-content-around">
                                <a href="" id="fb"><i class="fab fa-facebook"></i></a>
                                <a href="" id="ins"><i class="fab fa-instagram"></i></a>
                                <a href="" id="yt"><i class="fab fa-youtube"></i></a>
                            </ul>
                        </div>
                        <div class="col-md-4"></div>
                    </div>
                    <h3 class="text-center">&copy; Made by Miloš Ćirković </h3>
                </div>
                </div>
                </div>
                </div>
            </section>             
        </footer>	








        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>	
    </body>
</html>