let transferToggle = button => {
  let element = document.getElementById("transferTable");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
     element.removeAttribute("hidden");

  } else {
     element.setAttribute("hidden", "hidden");

  }
}

let pendingToggle = button => {
  let element = document.getElementById("pendingTable");
  let hidden = element.getAttribute("hidden");

  if (hidden) {
    element.removeAttribute("hidden");

  } else {
    element.setAttribute("hidden", "hidden");

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