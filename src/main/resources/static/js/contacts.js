const viewContactModal = document.getElementById("view_contact_modal");
const baseURL = "http://localhost:8080";

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_mdoal",
  override: true,
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);
function openContactModal() {
  contactModal.show();
}

function closeContactModal() {
  contactModal.hide();
}

async function loadContactData(id) {
  console.log(id);
  try {
    const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
    console.log(data);

    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
    document.querySelector("#contact_address").innerHTML = data.address;

    if (data.description) {
      document.querySelector("#contact_about").innerHTML = data.description;
    } else {
      document.querySelector("#contact_about").innerHTML = "No Description";
    }

    const contactFavourite = document.querySelector("#contact_favourite");

    if (data.favourite) {
      contactFavourite.innerHTML =
        "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
    } else {
      contactFavourite.innerHTML = "Not Favourite Contact";
    }

    if (data.instagramUsername) {
      document.querySelector("#contact_instagram").innerHTML =
        data.instagramUsername;
    } else {
      document.querySelector("#contact_instagram").innerHTML = "None";
    }

    if (data.linkedInLink) {
      document.querySelector("#contact_linkedIn").href = data.linkedInLink;
      document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
    } else {
      document.querySelector("#contact_linkedIn").innerHTML =
        "No Link Provided";
    }

    openContactModal();
  } catch (error) {
    console.log("Error: ", error);
  }
}

// sweetalert
async function deleteContact(id) {
  Swal.fire({
    title: "Do you want to delete the contact ?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
    confirmButtonColor: "#655cc9",
    cancelButtonColor: "#636c74",
  }).then((result) => {
    if (result.isConfirmed) {
      const url = `${baseURL}/user/contacts/delete/` + id;
      window.location.replace(url);
    }
  });
}

function exportData() {
  const table = document.getElementById("contact-table");

  // Create an array to hold the customized data
  let data = [];

  // Get all rows from the tbody (excluding header)
  const rows = table.querySelectorAll("tbody tr");

  rows.forEach((row) => {
    let rowData = [];

    // Extract Name
    const nameCell = row.querySelector("th");
    const name = nameCell
      ? nameCell.querySelector(".text-base").textContent.trim()
      : "";
    rowData.push(name); // Push name to rowData

    // Extract Email
    const email = nameCell
      ? nameCell.querySelector(".font-normal").textContent.trim()
      : "";
    rowData.push(email);

    // Extract Phone
    const phoneCell = row.querySelector("td:nth-child(2)");
    const phone = phoneCell ? phoneCell.textContent.trim() : "";
    rowData.push(phone);

    // Extract LinkedIn Link
    const linkCell = row.querySelector("td:nth-child(3)");
    const linkElement = linkCell ? linkCell.querySelector("a") : null;
    const link = linkElement ? linkElement.href : "No Link";
    rowData.push(link);

    // Check if Favourite
    const favCell = row.querySelector("td:nth-child(5)");
    const favourite =
      favCell && favCell.querySelector(".fa-star")
        ? "Favourite"
        : "Not Favourite";
    rowData.push(favourite);

    // Push the row data to the data array
    data.push(rowData);
  });

  // Create a new table for export with custom headers
  const exportTable = document.createElement("table");

  // Add custom headers to the export table
  const headerRow = exportTable.insertRow();
  headerRow.insertCell(0).textContent = "Name";
  headerRow.insertCell(1).textContent = "Email";
  headerRow.insertCell(2).textContent = "Phone";
  headerRow.insertCell(3).textContent = "Link";
  headerRow.insertCell(4).textContent = "Favourite";

  // Add the rows with extracted data
  data.forEach((rowData) => {
    const row = exportTable.insertRow();
    rowData.forEach((cellData, index) => {
      const cell = row.insertCell(index);
      cell.textContent = cellData;
    });
  });

  // Export the table as Excel
  TableToExcel.convert(exportTable, {
    name: "contacts.xlsx",
    sheet: {
      name: "Contacts",
    },
  });
}
