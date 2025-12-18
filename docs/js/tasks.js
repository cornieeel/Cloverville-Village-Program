document.addEventListener('DOMContentLoaded', () => {
  fetch('/trades.json')
    .then(response => response.json())
    .then(data => {
      const taskGrid = document.querySelector(".tasks-grid");

      data.forEach(item => {
        const article = document.createElement("article");
        article.className = "task-card";
        article.innerHTML = `
          <div class="task-icon-circle">
            <img
              src="images/tasks-symbol.png"
              alt="Task icon"
              class="task-icon"
            />
          </div>
          <div class="task-body">
            <h2 class="task-title">${item.goodsToOffer}</h2>
            <p class="task-owner">Owner: ${item.fullName}</p>
            <p class="task-points">${item.price} points</p>
          </div>
        `;
        taskGrid.appendChild(article);
      });
    });
});
