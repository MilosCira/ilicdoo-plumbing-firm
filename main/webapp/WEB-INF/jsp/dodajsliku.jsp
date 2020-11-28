<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/login.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/custom.css">
        <title>ilicdoo.com</title>

    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
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
        </nav>
        <nav class="navbar-default navbar-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="main-menu">

                     <li>
                        <a href="${pageContext.request.contextPath}/"><i class="fa fa-edit "></i>Početna</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikaziDodajAdmina"><i class="fa fa-edit "></i>Dodaj Admina</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikaziklijenta"><i class="fa fa-edit "></i>Klijenti</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikazirad"><i class="fa fa-edit "></i>Radovi</a>
                    </li>

                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikaziusluge"><i class="fa fa-edit "></i>Usluge</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikazidrzavu"><i class="fa fa-edit "></i>Država</a>
                    </li>

                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikazigrad"><i class="fa fa-edit "></i>Grad</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikazikomentar"><i class="fa fa-edit "></i>Komentari</a>
                    </li>
                    
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/prikazislike"><i class="fa fa-edit "></i>Slike radova</a>
                    </li>
                </ul>
            </div>
        </nav>


        <div id="page-wrapper" >
            <div id="page-inner">
                <div class="row">
                    <div class="col-md-12">

                    </div>
                    <div class="container">
                        <div class="panel-body">

                            <c:choose>
                                <c:when test="${izmena}">
                                   <!-- <form id="register-form" action="${pageContext.request.contextPath}/admin/updateteren?id=${teren.id}" method="post" role="form" enctype="multipart/form-data">-->
                                    </c:when>
                                    <c:otherwise>
                                        <form id="register-form" action="${pageContext.request.contextPath}/admin/dodajsliku" method="post" enctype="multipart/form-data">
                                        </c:otherwise>
                                    </c:choose>       

                                    
                                    <c:if test="${izmena==null}">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <label class="input-group-text" for="inputGroupSelect01">Izaberi rad</label>
                                        </div>
                                        <select name="radId">
                                            <c:forEach items="${radovi}" var="radovi">
                                                <option value=<c:out value="${radovi.id}"/> > <c:out value="${radovi.lokacija}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                                                  
                                    </c:if>
                                    <div class="input-group">
                                        <div class="custom-file">
                                            <input type="file" id="file" name="slika" class="custom-file-input" id="inputGroupFile04" >
                                          
                                            <label class="custom-file-label" for="inputGroupFile04">Izaberi Sliku</label>
                                        </div>
                                        
                                    </div>

                                    <div class="form-group">
                                        <div class="row">
                                            <c:if test="${izmena==null}">
                                                <div class="col-sm-6 col-sm-offset-3">
                                                    <input type="submit" name="register-submit" id="register-submit" tabindex="4" class="form-control btn btn-register" value="Dodaj Sliku">
                                                </div>
                                            </c:if>
                                            <c:if test="${izmena}">
                                                <div class="col-sm-6 col-sm-offset-3">
                                                    <input type="submit" name="register-submit" id="register-submit" tabindex="4" class="form-control btn btn-register" value="Izmeni Teren">
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </form>
                        </div>
                    </div>
                </div>              
                <hr/>

            </div>

        </div>

        <div class="footer">


            <div class="row">
                <div class="col-lg-12" id="fot">
                    &copy; Made by Miloš Ćirković
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script> 
    </body>
</html>