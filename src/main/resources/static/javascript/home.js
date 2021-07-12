var notebooks;
var notes;
var selectedNote;
var container = document.getElementById("container");
var btnContainer = document.getElementById("btnContainer");

var currentNotebookid;
jQuery.ajaxSetup({async:false});


$(document).ready(() => {
    runOnLoad();
});


function runOnLoad() {
    getNotebooks();
    currentNotebookid = null;
    container.innerHTML = "";
    container.style.height = "auto";
    for(let i = 0; i<notebooks.length; i++) {
        container.innerHTML += `
            <div class="item" id="" onclick="getNotes(${notebooks[i]['id']})">
              <h1>${notebooks[i]['name']}</h1>
            </div>
        `;
    }


    btnContainer.innerHTML = `
        <button class="btn btn-success newBtn" onclick="createNew(0);">Create new</button>
    `;
}

function getNotebooks() {
    $.get("/notebooks", (data) => {
        notebooks = data;
    });
}

function getNotes(id) {
    $.get("/notes?notebook_id=" + id, (data) => {
        notes = data;
    });
    container.innerHTML = "";
    for(let i = 0; i<notes.length; i++) {
        container.innerHTML += `
            <div class="item" id="" onclick="openNote(${notes[i]['id']})">
              <h1>${notes[i]['name']}</h1>
            </div>
        `;
    }
    btnContainer.innerHTML = `
        <button class="btn btn-success newBtn" onclick="createNew(1);">Create new</button>
    `;
    currentNotebookid = id;
}

function openNote(id) {
    $.get("/note?note_id=" + id, (data) => {
        selectedNote = data;
    });
    container.innerHTML = "";
    container.style.height = "500px";
    if(selectedNote['content'] != null) {
        container.innerHTML = `
        <textarea id="content">${selectedNote['content']}</textarea>
        <button class="btn btn-success" onclick="saveChanges(${selectedNote['id']})">Save</button>
    `;
    } else {
        container.innerHTML = `
        <textarea id="content"></textarea>
        <button class="btn btn-success" onclick="saveChanges(${selectedNote['id']})">Save</button>
    `;
    }

    btnContainer.innerHTML = ``;
}

function saveChanges(id) {
    let content = document.getElementById("content").value;
    let data = {
        'note_id': id,
        'content': content
    }
    $.post("/updateNote", data);
    runOnLoad();
}

function createNew(id) {
    if(id == 0) {
        // New Notebook
        container.innerHTML = "";
        container.innerHTML += `
            <input id="name" style="color:black;" type="text" placeholder="Name" required>
            <button class="btn btn-success" onclick="newNotebook();">Craete new</button>
        `;
        btnContainer.innerHTML = "";
    }

    if(id == 1) {
        // New Note
        container.innerHTML = "";
        container.innerHTML += `
            <input id="name" style="color:black;" type="text" placeholder="Name" required>
            <button class="btn btn-success" onclick="newNote();">Craete new</button>
        `;
        btnContainer.innerHTML = "";
    }
}


function newNotebook() {
    const name = document.getElementById("name").value;
    const url = "/newNotebook";
    data = {
        'name': name
    }
    $.post(url, data, () => {
        location.href = "/home";
    });
}

function newNote() {
    const name = document.getElementById("name").value;
    const notebook_id = currentNotebookid;
    data = {
        'name': name,
        'notebook_id': notebook_id
    }
    const url = "/newNote";
    $.post(url, data, () => {
        getNotes(notebook_id);
    });
}