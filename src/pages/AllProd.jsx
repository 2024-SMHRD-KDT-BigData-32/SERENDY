import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

const AllProd = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);

  const category = queryParams.get("category");
  const subCategory = queryParams.get("subCategory");

  const [gridType, setGridType] = useState("4x4");
  const [activeCategory, setActiveCategory] = useState("ALL");
  const [activeSubCategory, setActiveSubCategory] = useState("");
  const [currentPage, setCurrentPage] = useState(1);

  // 상품 데이터
  const allProducts = [
    ...Array(30).fill(null).map((_, index) => ({
      id: index + 1,
      image: "/placeholder.svg?height=300&width=250",
      category: "TOP",
      subCategory: ["탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티"][index % 7],
    })),
    ...Array(25).fill(null).map((_, index) => ({
      id: index + 31,
      image: "/placeholder.svg?height=300&width=250",
      category: "OUTER",
      subCategory: ["코트", "재킷", "점퍼", "패딩", "베스트", "가디건", "짚업"][index % 7],
    })),
    ...Array(28).fill(null).map((_, index) => ({
      id: index + 56,
      image: "/placeholder.svg?height=300&width=250",
      category: "BOTTOM",
      subCategory: ["청바지", "팬츠", "스커트", "레깅스", "조거팬츠"][index % 5],
    })),
    ...Array(22).fill(null).map((_, index) => ({
      id: index + 84,
      image: "/placeholder.svg?height=300&width=250",
      category: "ONEPIECE",
      subCategory: ["드레스", "점프수트"][index % 2],
    })),
  ];

  useEffect(() => {
    if (category) {
      setActiveCategory(category);
      setCurrentPage(1);
    }

    if (subCategory) {
      setActiveSubCategory(subCategory);
    } else {
      setActiveSubCategory("");
    }
  }, [category, subCategory]);

  const getItemsPerPage = () => {
    switch (gridType) {
      case "3x3": return 9;
      case "4x4": return 16;
      case "5x5": return 25;
      default: return 9;
    }
  };

  const handleGridTypeChange = (type) => {
    setGridType(type);
    setCurrentPage(1);
  };

  const getFilteredProducts = () => {
    if (!activeCategory || activeCategory === "ALL") return allProducts;

    return allProducts.filter(product => {
      const categoryMatch = product.category === activeCategory;
      const subMatch = activeSubCategory ? product.subCategory === activeSubCategory : true;
      return categoryMatch && subMatch;
    });
  };

  const getCurrentPageProducts = () => {
    const filtered = getFilteredProducts();
    const itemsPerPage = getItemsPerPage();
    const start = (currentPage - 1) * itemsPerPage;
    return filtered.slice(start, start + itemsPerPage);
  };

  const getTotalPages = () => {
    return Math.ceil(getFilteredProducts().length / getItemsPerPage());
  };

  const getVisiblePages = () => {
    const total = getTotalPages();
    const maxPagesToShow = 5;
    const half = Math.floor(maxPagesToShow / 2);

    let start = Math.max(1, currentPage - half);
    let end = start + maxPagesToShow - 1;

    if (end > total) {
      end = total;
      start = Math.max(1, end - maxPagesToShow + 1);
    }

    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const scrollToBottom = () => {
    window.scrollTo({ top: document.documentElement.scrollHeight, behavior: "smooth" });
  };

  return (
    <div className="arpFrame">
      <div className="arpContainer">
        {/* 선택된 카테고리 정보 표시 */}
        {category && (
          <div className='categoryInfo'>
            {subCategory ? (
              <>
                {category} <span className="categoryArrow">&gt;</span> {subCategory}
              </>
            ) : (
              category
            )}
          </div>
        )}

        {/* 그리드 타입 버튼 */}
        <div className="gridBtn">
          {["3x3", "4x4", "5x5"].map((type) => (
            <div key={type}
              className={`gridIcon ${gridType === type ? "active" : ""}`}
              onClick={() => handleGridTypeChange(type)}>
              <img src={`/imgs/${type.replace("x", "")}grid.png`} alt={`${type} 그리드`} className="gridImg" />
            </div>
          ))}
        </div>

        {/* 상품 목록 */}
        <div className={`productsGrid grid-${gridType}`}>
          {getCurrentPageProducts().map((product) => (
            <div key={product.id} className="productCard">
              <img className="prodImg" src={product.image} alt="상품 이미지" />
              <Link to="" className="detailBtn">Detail<span className="detailBtnArrow">→</span></Link>
            </div>
          ))}
        </div>

        {/* 페이지이동 */}
        {getTotalPages() > 1 && (
          <div className="pageNav">
            {/* << 맨 처음 */}
            <button
              className="pageNavBtn first"
              onClick={() => handlePageChange(1)}
              disabled={currentPage === 1}
            >
              <img className='pageDoubleLeftArrow' src="/imgs/simpleDoubleArrow.png" alt="맨 처음" />
            </button>

            {/* < 이전 */}
            <button
              className="pageNavBtn prev"
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
            >
              <img className='pageLeftArrow' src="/imgs/simpleArrow.png" alt="이전" />
            </button>

            {/* 페이지 번호 */}
            <div className="pageNums">
              {getVisiblePages().map((page) => (
                <button
                  key={page}
                  className={`pageNum ${currentPage === page ? "active" : ""}`}
                  onClick={() => handlePageChange(page)}
                >
                  {page}
                </button>
              ))}
            </div>

            {/* > 다음 */}
            <button
              className="pageNavBtn next"
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === getTotalPages()}
            >
              <img className='pageRightArrow' src="/imgs/simpleArrow.png" alt="다음" />
            </button>

            {/* >> 맨 끝 */}
            <button
              className="pageNavBtn last"
              onClick={() => handlePageChange(getTotalPages())}
              disabled={currentPage === getTotalPages()}
            >
              <img className='pageDoubleRightArrow' src="/imgs/simpleDoubleArrow.png" alt="맨 끝" />
            </button>
          </div>
        )}

        {/* 스크롤 버튼 */}
        <div className="navArrows">

          <button className="navArrow up" onClick={scrollToTop}>
            <img src="/imgs/화살표.png" alt="위로" className="arrowImg arrowUp" />
          </button>

          <button className="navArrow down" onClick={scrollToBottom}>
            <img src="/imgs/화살표.png" alt="아래로" className="arrowImg arrowDown" />
          </button>
          
        </div>
      </div>
    </div>
  );
};

export default AllProd;