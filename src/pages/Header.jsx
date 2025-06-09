import { Link, Links, useNavigate } from 'react-router-dom';
import '../css/Header.css';
import { useState } from 'react';

const categoryData = {
  "TOP": ["탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티"],
  "BOTTOM": ["청바지", "팬츠", "스커트", "레깅스", "조거팬츠"],
  "OUTER": ["코트", "재킷", "점퍼", "패딩", "베스트", "가디건", "짚업"],
  "ONEPIECE": ["드레스", "점프수트"]
};

const Header = () => {
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState('');
  const [hoveredCategory, setHoveredCategory] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState('');

  const handleSearch = () => {
    if (searchTerm.trim() === '') return;
    console.log('검색어:', searchTerm);
    // 예: navigate(`/search?query=${searchTerm}`);
  };

  const handleNavigate = (path, categoryName) => {
    setSelectedCategory(categoryName);
    navigate(path);
  };

  const handleCategoryClick = (category) => {
    setSelectedCategory(category);
    navigate(`/allprod?category=${category}`);
  };

  const handleSubCategoryClick = (category, subCategory) => {
    setSelectedCategory(`${category}-${subCategory}`);
    navigate(`/allprod?category=${category}&subCategory=${subCategory}`);
  };

  return (
    <header>

      <Link to='/mainrecomdprod' className="logo">Serendy</Link>

      <nav className="categoryNav">
        {/* 추천 & 전체 탭 */}
        <div
          className={`categoryItem ${selectedCategory === 'RECOMMEND' ? 'active' : ''}`}
          onClick={() => handleNavigate('/mainrecomdprod', 'RECOMMEND')}
        >
          RECOMMEND
        </div>
        <div
          className={`categoryItem ${selectedCategory === 'ALL' ? 'active' : ''}`}
          onClick={() => handleNavigate('/allprod?category=ALL', 'ALL')}
        >
          ALL
        </div>

        {/* 상위 카테고리들 */}
        {Object.entries(categoryData).map(([category, subCategories]) => (
          <div
            key={category}
            className="categoryItemWrapper"
            onMouseEnter={() => setHoveredCategory(category)}
            onMouseLeave={() => setHoveredCategory(null)}
          >
            <div
              className={`categoryItem ${selectedCategory === category ? 'active' : ''}`}
              onClick={() => handleCategoryClick(category)}
            >
              {category}
            </div>

            {hoveredCategory === category && (
              <div className="subCategoryDropdown">
                {subCategories.map((sub) => (
                  <div
                    key={sub}
                    className="subCategoryItem"
                    onClick={() => handleSubCategoryClick(category, sub)}
                  >
                    {sub}
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </nav>

      <div className="searchBox">
        <input
          type="text"
          className="searchInput"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button className="searchBtn" onClick={handleSearch}>
          <img src="/imgs/search.png" className="searchIcon" alt="검색" />
        </button>
      </div>

      <Link to='/mypagemenu'>
        <img className='mypageImg' src="/imgs/mypage.png" alt="마이페이지" />
      </Link>

      <button className="headerMenuLogout">
        <img className='logoutImg' src="/imgs/logout.png" alt="로그아웃" />
      </button>
      
    </header>
  );
};

export default Header;