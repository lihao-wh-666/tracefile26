(function() {
    var toggle = document.getElementById('sidebarToggle');
    var sidebar = document.getElementById('sidebar');
    var overlay = document.getElementById('sidebarOverlay');

    if (toggle && sidebar) {
        toggle.addEventListener('click', function() {
            sidebar.classList.toggle('open');
            if (overlay) overlay.classList.toggle('show');
        });
    }

    if (overlay) {
        overlay.addEventListener('click', function() {
            if (sidebar) sidebar.classList.remove('open');
            overlay.classList.remove('show');
        });
    }

    window.addEventListener('resize', function() {
        if (window.innerWidth > 1024) {
            if (sidebar) sidebar.classList.remove('open');
            if (overlay) overlay.classList.remove('show');
        }
    });
})();
