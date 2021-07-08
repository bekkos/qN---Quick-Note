var notebooks;
var notes;
var selectedNote;
var container = document.getElementById("container");
jQuery.ajaxSetup({async:false});


$(document).ready(() => {
    runOnLoad();
});


function runOnLoad() {
    getNotebooks();
    container.innerHTML = "";
    for(let i = 0; i<notebooks.length; i++) {
        container.innerHTML += `
            <div class="item" id="" onclick="getNotes(${notebooks[i]['id']})">
              <h1>${notebooks[i]['name']}</h1>
            </div>
        `;
    }
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
}

function openNote(id) {
    $.get("/note?note_id=" + id, (data) => {
        selectedNote = data;
    });
    container.innerHTML = "";
    container.innerHTML = `
        <textarea id="content">${selectedNote['content']}</textarea>
        <button class="btn btn-success" onclick="saveChanges(${selectedNote['id']})">Save</button>
    `;
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