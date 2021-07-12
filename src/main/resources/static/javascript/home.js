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
    if(notebooks.length != 0) {
        for(let i = 0; i<notebooks.length; i++) {
            container.innerHTML += `
            <div class="item" id="" onclick="getNotes(${notebooks[i]['id']})">
              <h1>${notebooks[i]['name']}</h1>
            </div>
        `;
        }
    } else {
        container.innerHTML = `
            <p style="text-align: center">There is nothing here. <br>Get started by creating a new notebook!</p>
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
    if(notes.length != 0) {
        for(let i = 0; i<notes.length; i++) {
            container.innerHTML += `
            <div class="item" id="" onclick="openNote(${notes[i]['id']})">
              <h1>${notes[i]['name']}</h1>
            </div>
        `;
        }
    } else {
        container.innerHTML = `
            <p style="text-align: center">Add a note to your notebook!</p>
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
        <textarea id="content" placeholder="Start typing your note here."></textarea>
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
    getNotes(currentNotebookid);
}

function createNew(id) {
    if(id == 0) {
        // New Notebook
        container.innerHTML = "";
        container.innerHTML += `
            <form autocomplete="off">
                <input id="name" class="form-control" style="color:black; margin:auto;" type="text" placeholder="Name" required>
                <br><br>
                <button class="btn btn-success form-control" style="margin:auto;" onclick="newNotebook();">Create new</button>
                <br>
                <p id="errorMsg"></p>
            </form>
            
        `;
        btnContainer.innerHTML = "";
    }

    if(id == 1) {
        // New Note
        container.innerHTML = "";
        container.innerHTML += `
            <form autocomplete="off">
                <input id="name" class="form-control" style="color:black; margin:auto;" type="text" placeholder="Name" required>
                <br><br>
                <button class="btn btn-success form-control" style="margin:auto;" onclick="newNote();">Create new</button>
                <br>
                <p id="errorMsg"></p>
            </form>
        `;
        btnContainer.innerHTML = "";
    }
}


function newNotebook() {
    const name = document.getElementById("name").value;
    if(name.length <= 0) {
        let errorMsg = document.getElementById("errorMsg");
        errorMsg.innerText = "Error: Name can not be empty.";
        return;
    }
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
    if(name.length <= 0) {
        let errorMsg = document.getElementById("errorMsg");
        errorMsg.innerText = "Error: Name can not be empty.";
        return;
    }
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