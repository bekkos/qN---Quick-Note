// GLOBALS
let notebooks;
let notes;
let selectedNote;
let container = document.getElementById("container");
let buttonContainer = document.getElementById("btnContainer");
let currentNotebookId;
let latexEditorState = -1;


// Configurations
jQuery.ajaxSetup({async:false});


// Server calls
const getNotebooks = () => {
    $.get("/notebooks", (data) => {
        notebooks = data;
    });
}

const getNotes = (id) => {
    currentNotebookId = id;
    $.get("/notes?notebook_id=" + id, (data) => {
        notes = data;
    });
    displayNotes(id);
}


// DOM Altering
const clearContainer = () => {
    container.innerHTML = "";
}

const clearButtonContainer = () => {
    buttonContainer.innerHTML = "";
}

const resetEditorControls = () => {
    let editorControls = document.getElementById("editor-controls");
    editorControls.style.display = "none";
    container.style.maxWidth = "340px";
    container.style.display = "block";
}

const displayNotebooks = () => {
    getNotebooks();
    currentNotebookid = null;

    clearContainer();
    clearButtonContainer();
    resetEditorControls();

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

    buttonContainer.innerHTML = `
        <button class="btn btn-success newBtn" onclick="openCreateNewForm(0);">Create new</button>
    `;
}

const displayNotes = (id) => {
    clearContainer();

    if(notes.length <= 0) {
        container.innerHTML = `
            <p style="text-align: center">Add a note to your notebook!</p>
        `;
    } else {
        for(let i = 0; i<notes.length; i++) {
            container.innerHTML += `
            <div class="item" id="" onclick="openNote(${notes[i]['id']})">
                <h1>${notes[i]['name']}</h1>
            </div>
            `;
        }
    }

    btnContainer.innerHTML = `
        <button class="btn btn-success newBtn" onclick="openCreateNewForm(1);">Create new</button>
    `;
    currentNotebookid = id;

}

const openNote = (id) => {
    $.get("/note?note_id=" + id, (data) => {
        selectedNote = data;
    });

    clearContainer();
    clearButtonContainer();

    let editorControls = document.getElementById("editor-controls");
    editorControls.style.display = "flex";
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

}

const saveChanges = (id) => {
    let content = document.getElementById("content").value;

    let data = {
        'note_id': id,
        'content': content
    }

    $.post("/updateNote", data);



    resetEditorControls();
    getNotes(currentNotebookId);
}

const openCreateNewForm = (id) => {
    if(id == null) return;

    if(id == 0) {
        // For notebook creation
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
        clearButtonContainer();
    }

    if(id == 1) {
        // For note creation
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
        clearButtonContainer();
    }
}

const newNotebook = () => {
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

const newNote = () => {
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


const enableLatexEditor = () => {
    let rawTextEditor = document.getElementById("content");

    container.style.maxWidth = "680px";
    container.style.display = "flex";
    container.innerHTML = `
        <textarea id="content" style="width:339px !important; display: inline-block !important; text-align: center;background-color: #282525;">${rawTextEditor.value}</textarea>
        <div id="parsed" style="width:339px !important; display: inline-block !important; text-align: center; background-color: #5c584a;" disabled>${rawTextEditor.value}</div>
    `;

    buttonContainer.innerHTML = `
        <button class="btn btn-success" style="height: 40% !important;" onclick="saveChanges(${selectedNote['id']})">Save</button>
    `;

    let parser = document.getElementById("parsed");
    let content = document.getElementById("content");

    const parse = () => {
        parser.innerText = content.value;
        MathJax.typeset([parsed]);
    }

    document.addEventListener("keyup", parse);
    MathJax.typeset([parsed]);
}

const disableLatexEditor = () => {
    saveChanges(selectedNote['id']);
    openNote(selectedNote['id']);
}

const toggleLatexEditor = () => {
    if(latexEditorState == -1) {
        enableLatexEditor();
        latexEditorState = -latexEditorState;
    } else {
        disableLatexEditor();
        latexEditorState = -latexEditorState;
    }
}



// Run-on-load
displayNotebooks();