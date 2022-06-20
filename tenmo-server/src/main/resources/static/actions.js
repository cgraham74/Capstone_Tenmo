let toggle = button => {
  let element = document.getElementById("transferTable");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
     element.removeAttribute("hidden");
     button.innerText = "Hide Transfers";
  } else {
     element.setAttribute("hidden", "hidden");
     button.innerText = "Show Transfers";
  }
}
let pendingToggle = button => {
  let element = document.getElementById("pendingtable");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
    element.removeAttribute("hidden");
    button.innerText = "Hide Pending";
  } else {
    element.setAttribute("hidden", "hidden");
    button.innerText = "Show Pending";
  }
}
let toggleApprove = button => {
  let element = document.getElementById("approve");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
    element.removeAttribute("hidden");
  } else {
    element.setAttribute("hidden", "hidden");
  }
}
let toggleReject = button => {
  let element = document.getElementById("reject");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
    element.removeAttribute("hidden");
  } else {
    element.setAttribute("hidden", "hidden");
  }
}