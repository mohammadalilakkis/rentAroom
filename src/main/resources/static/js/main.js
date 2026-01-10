// Set minimum date for check-in to today
document.addEventListener('DOMContentLoaded', function() {
    const checkInInput = document.getElementById('checkIn');
    const checkOutInput = document.getElementById('checkOut');
    
    if (checkInInput) {
        const today = new Date().toISOString().split('T')[0];
        checkInInput.setAttribute('min', today);
        
        checkInInput.addEventListener('change', function() {
            if (checkOutInput) {
                checkOutInput.setAttribute('min', this.value);
                if (checkOutInput.value && checkOutInput.value < this.value) {
                    checkOutInput.value = '';
                }
            }
        });
    }
    
    // Form validation
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
});

// Auto-hide alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});
