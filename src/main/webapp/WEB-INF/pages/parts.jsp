<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
  Created by IntelliJ IDEA.
  User: Serzh
  Date: 21-May-19
  Time: 1:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Parts</title>
    <spring:url value="/resources/w3.css" var="styleSheetUrl" />
    <link rel="stylesheet" href="${styleSheetUrl}">
    <c:url value="/add" var="addUrl" />
    <c:url value="/applyOrdering/" var="applyOrdering" />

    <script type="text/javascript">
        function openUpdateModal(id, essential)
        {
            document.getElementById('id01').style.display='block';
            document.getElementById('modalHeader').innerText = 'Редактировать';
            document.getElementById('name').value = document.getElementById('partName' + id).innerText;
            document.getElementById('count').value = document.getElementById('partCount' + id).innerText;
            document.getElementById('selectSwitcher').checked = essential;
            document.getElementById('typeSelector').disabled = !essential;
            document.getElementById('typeSelector').value = document.getElementById("partType" + id).value;
            document.getElementById('addForm').action = '/update/'+id;
        }
        function openAddModal() {
            document.getElementById('id01').style.display='block';
            document.getElementById('modalHeader').innerText = 'Добавить';
            document.getElementById('name').value = '';
            document.getElementById('count').value = 1;
            document.getElementById('selectSwitcher').checked = false;
            document.getElementById('typeSelector').disabled = true;
            document.getElementById('typeSelector').value = '';
            document.getElementById('addForm').action = "/add";
        }
        function performSearch() {
            window.location.hash=document.getElementById('searchName').value.toUpperCase().trim();
        }
        function switchSelect() {
            var typeSelect = document.getElementById("typeSelector");
            typeSelect.disabled = !(typeSelect.disabled);
        }
    </script>
</head>
<body style="font-family:'Lucida Sans Unicode',sans-serif;background-color:whitesmoke;">

<div class="w3-container w3-teal">             <!-- Header container -->
    <h1>Parts List</h1>
</div>

<div class="w3-display-container w3-center ">                     <!-- Content container -->
    <div class="w3-container">
        <div class="w3-container w3-left">
            <p id="addMessage" style="font-size:14pt;font-family:'Courier New',monospace;">${flashMessage}</p>
        </div>

        <c:choose>
            <c:when test="${isNoElementsToShow}">
                <p><strong>There are no parts to show. You may either add some by the button "add" below or exit and
                    forget our address.</strong></p>
            </c:when>
            <c:otherwise>
                <form class="w3-container w3-right" onSubmit="performSearch()" style="padding-top:1%;">
                    <input class="w3-input" type="text" id="searchName" required="required"/>
                    <input class="w3-btn w3-hover-blue-grey" type="submit" value="Поиск">
                </form>
                <table class="w3-table-all w3-centered w3-padding" style="min-width:900px;">
                    <tr>
                        <th style="width:15%;">Наименование
                            <a href="${applyOrdering}nameASC">▲</a>
                            <a href="${applyOrdering}nameDESC">▼</a></th></th>
                        <th style="width:10%;">Необходимость
                            <a href="${applyOrdering}essentialASC">▲</a>
                            <a href="${applyOrdering}essentialDESC">▼</a></th>
                        <th style="width:10%;">Количество
                            <a href="${applyOrdering}countASC">▲</a>
                            <a href="${applyOrdering}countDESC">▼</a></th></th>
                        <th style="width:20%;">Правка</th>
                    </tr>
                    <c:forEach var="partItem" items="${partsList}">
                        <tr id="${partItem.name.toUpperCase()}">
                            <td id="partName${partItem.id}">${partItem.name}</td>
                            <td ><span id="partEssential${partItem.id}">${partItem.essential}</span></td>
                            <td ><span id="partCount${partItem.id}" style="padding-right:10px;">${partItem.count}</span>
                                <a class="w3-button w3-pale-red w3-round-xxlarge" href="/deleteOne/${partItem.id}">-</a>
                                <a class="w3-button w3-pale-green w3-round-xxlarge" href="/addOne/${partItem.id}">+</a>
                            </td>
                            <td >
                                <input id="partType${partItem.id}" type="hidden" value="${partItem.type}" />
                                <button class="w3-button" onclick="openUpdateModal(${partItem.id}, ${partItem.essential})">редактировать</button>
                                <a class="w3-button" href="/delete/${partItem.id}">удалить</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr class="w3-card-4 w3-padding">
                        <td>Можно собрать</td>
                        <td>${setupsCount}</td>
                        <td colspan="2">компьютеров</td>
                    </tr>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="w3-container">
        <div class="w3-container w3-left w3-bar-item" style="padding-bottom:25px;padding-top:15px;">
            <!-- Buttons Container -->

            <button onclick="openAddModal()"
                    class="w3-btn w3-light-gray w3-animate-opacity w3-hover-light-blue w3-round-large">
                Добавить
            </button>
            <button onclick="location.href='/'"
                    class="w3-btn w3-light-gray w3-animate-opacity w3-hover-light-blue w3-round-large">
                Обновить страницу
            </button>
        </div>
        <div class="w3-container w3-center">
            Страница:
            <c:forEach begin="1" end="${pagesCount}" step="1" varStatus="i">
                <c:url value="/" var="pageUrl">
                    <c:param name="page" value="${i.index}"/>
                    <c:param name="resultsCount" value="${resultsCount}" />
                </c:url>
                <c:choose>
                    <c:when test="${page == i.index}">
                        <a href="" class="w3-round-large w3-light-green w3-opacity-min w3-padding">${i.index}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageUrl}" class=" w3-padding">${i.index}</a>
                    </c:otherwise>
                </c:choose>

            </c:forEach>
            / ${pagesCount}

            <p >На странице:
                <c:forEach begin="10" end="50" step="10" varStatus="j">
                    <c:url value="/" var="countUrl">
                        <c:param name="resultsCount" value="${j.index}"/>
                    </c:url>
                    <a href="${countUrl}">${j.index}</a>
                </c:forEach>
                / ${partsCount} </p>
        </div>
    </div>

</div>

<!-- The Modal -->
<div id="id01" class="w3-modal">
    <div class="w3-modal-content w3-card-4 w3-animate-opacity">
        <!-- head of the modal -->
        <header class="w3-container w3-teal">
            <span onclick="document.getElementById('id01').style.display='none'"
                  class="w3-button w3-display-topright">&times;</span>
            <h2 class="w3-center" id="modalHeader">Добавить</h2>
        </header>
        <!-- body of the modal -->
        <div class="w3-container">
            <form class="w3-container" onSubmit="${addUrl}" method="POST" id="addForm">
                <p>
                    <input type="hidden" id="idForUpdate" value="0"/>
                    <label for="name">Название:</label>
                    <input class="w3-input" type="text" name="name" id="name" maxlength="100"
                           autofocus="autofocus" required="required"/>
                </p>
                <p>
                    <label for="count">Количество:</label>
                    <input class="w3-input" type="number" name="count" value="1" min="0" maxlength="10" id="count"/>
                </p>
                <p>
                    <label class="label" for="selectSwitcher">Необходима для сборки?</label>
                    <input class="w3-check" type="checkbox" onchange="switchSelect()" id="selectSwitcher"/>
                </p>
                <p>
                    <label for="typeSelector">Тип детали:</label>
                    <select class="w3-select" name="type" form="addForm" id="typeSelector" disabled="disabled">
                        <option value="" selected="selected" disabled="disabled">Выберите тип делали</option>
                        <c:forEach items="${essentialPartTypes}" var="typeSelector">
                            <option value="${typeSelector}">${typeSelector.name}</option>
                        </c:forEach>
                    </select>
                </p>
                <footer class="w3-container">                                        <!-- footer of the modal -->
                    <div class="w3-container w3-center">
                        <input class="w3-btn w3-light-gray w3-animate-opacity w3-hover-light-blue w3-round-large w3-left"
                               type="submit" value="Submit"/>
                    </div>
                </footer>
            </form>
        </div>
    </div>
</div>

</body>
</html>
