let portfolioData = [];

document.addEventListener('DOMContentLoaded', () => {
    const boardList = document.getElementById('portfolio-board');
    const navLinks = document.querySelectorAll('.nav-link');
    const chkAll = document.getElementById('chk-all');
    const deleteBtn = document.getElementById('delete-btn');

    // Filter elements
    const filterYear = document.getElementById('filter-year');
    const filterCategory = document.getElementById('filter-category');
    const filterTitle = document.getElementById('filter-title');
    const filterStart = document.getElementById('filter-start');
    const filterEnd = document.getElementById('filter-end');
    const btnSearch = document.getElementById('btn-search');
    const btnReset = document.getElementById('btn-reset');

    const loadData = async () => {
        try {
            const response = await fetch('/api/portfolio');
            portfolioData = await response.json();
            portfolioData.sort((a, b) => b.id - a.id);
            
            initFilterOptions();
            renderBoard(portfolioData);
        } catch (error) {
            console.error("Error loading data:", error);
        }
    };

    const initFilterOptions = () => {
        const years = [...new Set(portfolioData.map(item => item.year).filter(Boolean))].sort().reverse();
        const categories = [...new Set(portfolioData.map(item => item.category).filter(Boolean))].sort();

        // Keep the default "전체" option and append others
        filterYear.innerHTML = '<option value="">전체</option>';
        years.forEach(year => {
            filterYear.innerHTML += `<option value="${year}">${year}</option>`;
        });

        filterCategory.innerHTML = '<option value="">전체</option>';
        categories.forEach(cat => {
            filterCategory.innerHTML += `<option value="${cat}">${cat}</option>`;
        });
    };

    const renderBoard = (data) => {
        boardList.innerHTML = '';
        if (chkAll) chkAll.checked = false;
        
        if(data.length === 0) {
            boardList.innerHTML = '<div style="padding: 2rem; text-align: center; color: var(--text-muted);">해당 조건의 프로젝트가 없습니다.</div>';
            return;
        }

        data.forEach((item, index) => {
            const row = document.createElement('div');
            row.className = 'board-item';
            
            let techArray = Array.isArray(item.techStack) ? item.techStack : (item.techStack ? item.techStack.split(',').map(s => s.trim()) : []);
            const techBadges = techArray.map(tech => `<span class="tech-badge">${tech}</span>`).join('');

            // If chkAll exists, it means the user is authenticated (admin)
            const chkCol = chkAll ? `<div class="col col-chk" onclick="event.stopPropagation()"><input type="checkbox" class="chk-item" value="${item.id}"></div>` : '';

            row.innerHTML = `
                ${chkCol}
                <div class="col col-year" onclick="location.href='/detail/${item.id}'" style="cursor:pointer">${item.year}</div>
                <div class="col col-title" onclick="location.href='/detail/${item.id}'" style="cursor:pointer">${item.title}</div>
                <div class="col col-tech" onclick="location.href='/detail/${item.id}'" style="cursor:pointer">${techBadges}</div>
                <div class="col col-date" onclick="location.href='/detail/${item.id}'" style="cursor:pointer">${item.startDate} ~ ${item.endDate}</div>
            `;
            
            row.style.animation = 'fadeInUp 0.5s ease backwards';
            row.style.animationDelay = `${index * 0.05}s`;

            boardList.appendChild(row);
        });
    };

    const validateDate = (dateStr) => {
        if (!dateStr) return true;
        // YYYY.MM format check
        const regex = /^\d{4}\.(0[1-9]|1[0-2])$/;
        return regex.test(dateStr);
    };

    const applyFilters = () => {
        let filteredData = portfolioData;

        const yVal = filterYear.value;
        const cVal = filterCategory.value;
        const tVal = filterTitle.value.trim().toLowerCase();
        const sVal = filterStart.value.trim();
        const eVal = filterEnd.value.trim();

        if (!validateDate(sVal) || !validateDate(eVal)) {
            alert('일자를 확인해주세요. (형식: YYYY.MM)');
            return;
        }

        if (yVal) {
            filteredData = filteredData.filter(item => item.year === yVal);
        }
        if (cVal) {
            filteredData = filteredData.filter(item => item.category === cVal);
        }
        if (tVal) {
            filteredData = filteredData.filter(item => 
                item.title && item.title.toLowerCase().includes(tVal)
            );
        }
        if (sVal) {
            filteredData = filteredData.filter(item => item.startDate && item.startDate >= sVal);
        }
        if (eVal) {
            filteredData = filteredData.filter(item => item.endDate && item.endDate <= eVal);
        }

        renderBoard(filteredData);
    };

    // Events for buttons
    if (btnSearch) {
        btnSearch.addEventListener('click', applyFilters);
    }

    if (btnReset) {
        btnReset.addEventListener('click', () => {
            filterYear.value = '';
            filterCategory.value = '';
            filterTitle.value = '';
            filterStart.value = '';
            filterEnd.value = '';
            
            // Reset nav links styling
            navLinks.forEach(nav => nav.classList.remove('active'));
            if(navLinks.length > 0) navLinks[0].classList.add('active'); // set "전체" active
            
            applyFilters();
        });
    }

    // Sync top nav links with category select box
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navLinks.forEach(nav => nav.classList.remove('active'));
            link.classList.add('active');

            const category = link.getAttribute('data-filter') || link.textContent;
            
            if (category === '전체') {
                filterCategory.value = '';
            } else {
                // If the option exists, select it
                const optionExists = Array.from(filterCategory.options).some(opt => opt.value === category);
                if(optionExists) {
                    filterCategory.value = category;
                }
            }
            applyFilters();
        });
    });

    if (chkAll) {
        chkAll.addEventListener('change', (e) => {
            const chkItems = document.querySelectorAll('.chk-item');
            chkItems.forEach(chk => chk.checked = e.target.checked);
        });
    }

    if (deleteBtn) {
        deleteBtn.addEventListener('click', async () => {
            const checked = document.querySelectorAll('.chk-item:checked');
            if (checked.length === 0) {
                alert('삭제할 항목을 선택해주세요.');
                return;
            }

            if (!confirm(`선택한 ${checked.length}개의 내역을 삭제하시겠습니까?`)) {
                return;
            }

            const ids = Array.from(checked).map(chk => parseInt(chk.value));

            try {
                const response = await fetch('/api/portfolio', {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(ids)
                });
                
                if (response.ok) {
                    alert('삭제되었습니다.');
                    loadData();
                } else {
                    alert('삭제 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error("Error deleting:", error);
                alert('에러가 발생했습니다.');
            }
        });
    }

    loadData();
});
