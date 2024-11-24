console.log("hey babayy!");

let currentTheme = getTheme();

// will call changeTheme() when document loaded on 
document.addEventListener("DOMContentLoaded",()=>{
    changeTheme();
})

function changeTheme() {
  changePageTheme(currentTheme, currentTheme);

  // set the listener to change the theme
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", (e) => {
    console.log("button clicked");
    let oldTheme = currentTheme;
    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }

    changePageTheme(currentTheme, oldTheme);
  });
}

// set theme to localstorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

//get from localstorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// change current page theme
function changePageTheme(theme, oldTheme) {
  //updating the new theme
  setTheme(currentTheme);
  //remove the current theme from class
  document.querySelector("html").classList.remove(oldTheme);
  //set the current theme
  document.querySelector("html").classList.add(currentTheme);

  // change the text of theme button
  document
    .querySelector("#theme_change_button")
    .querySelector("span").textContent = theme == "light" ? "Dark" : "Light";
}


// --------------------- XXXXXXX ----------------------- 
// --------------------- XXXXXXX ----------------------- 


// Add Contact form -> Image preview code

document.querySelector("#image_file_input")
        .addEventListener("change", function(e){
          let file = e.target.files[0];
          let reader = new FileReader();
          reader.onload = function(){
            document.querySelector("#upload_image_preview").setAttribute("src", reader.result);
          };
          reader.readAsDataURL(file);
        });


// --------------------- XXXXXXX ----------------------- 
// --------------------- XXXXXXX ----------------------- 

// view contact modal code


async function loadContactData(id){
  console.log("id: " + id);
  try {
    const data = await (
      await fetch(`http://localhost:8081/api/contact/${id}`)
    ).json();
    console.log("contact data: " + data);
    console.log("contact data name: " + data.name);

    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_address").innerHTML = data.address;
    document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
    document.querySelector("#contact_desc").innerHTML = data.description;
    document.querySelector("#contact_favourite").innerHTML = data.favourite;




  } catch (error) {
    console.log("error: " + error);
  }
}

function deleteContact(id){
  console.log("id: " +id);
  const deleteConfirm = confirm("sure you want to delete this contact");
    if (deleteConfirm) {
      // If the user confirms, call the delete API
      fetch(`http://localhost:8081/api/contact/delete/${id}`, {
          method: 'DELETE',
      })
      .then(response => {
          if (response.ok) {
              alert("Item deleted successfully.");
              location.reload(); // Reloads the page
          } else {
              alert("Failed to delete the item.");
          }
      })
      .catch(error => {
          console.error("Error deleting item:", error);
          alert("An error occurred while trying to delete the item.");
      });
  } else {
      // If user clicks "Cancel," simply return and do nothing
      return;
  }
}

// --------------------- XXXXXXX ----------------------- 
// --------------------- XXXXXXX ----------------------- 

// export data into excel file i,e., get all contacts by user

function exportData(){

    TableToExcel.convert(document.getElementById("contact_table"), {
      name : "contacts.xlsx",
      sheet : {
        name : "sheet 1",
      },
    });


}


// --------------------- XXXXXXX ----------------------- 
// --------------------- XXXXXXX ----------------------- 