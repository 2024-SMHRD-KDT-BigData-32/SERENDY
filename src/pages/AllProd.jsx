import axios from 'axios';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const AllProd = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);

  const category = queryParams.get("category");
  const subCategory = queryParams.get("subCategory");

  const [gridType, setGridType] = useState("4x4");
  const [currentPage, setCurrentPage] = useState(1);

  const [products, setProducts] = useState([]);

  useEffect(() => {
    setCurrentPage(1);
  }, [category, subCategory]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const categoryMap = {
          TOP: "상의",
          BOTTOM: "하의",
          OUTER: "아우터",
          ONEPIECE: "원피스"
        };

        const areaCategory = categoryMap[category] || category;

        if (!category || category === "ALL") {
          // 전체 상품 호출
          const res = await axios.get('/api/products/all');
          setProducts(res.data);
        } else {
          // 카테고리별 호출
          const params = {
            area: areaCategory
          };
          if (subCategory) {
            params.sub = subCategory;
          }

          const res = await axios.get('/api/products/byCategory', { params });
          setProducts(res.data);
          console.log('카테고리 상품:', res.data);
        }
      } catch (error) {
        console.error("상품 불러오기 실패:", error);
        setProducts([]); // 에러시 빈배열 처리
      }
    };

    fetchProducts();
  }, [category, subCategory]);

  // 그리드 타입에 따른 페이지당 상품 개수
  const getItemsPerPage = () => {
    switch (gridType) {
      case "3x3": return 9;
      case "4x4": return 16;
      case "5x5": return 25;
      default: return 9;
    }
  };

  // 그리드 타입 변경
  const handleGridTypeChange = (type) => {
    setGridType(type);
    setCurrentPage(1);
  };

  const getCurrentPageProducts = () => {
    const itemsPerPage = getItemsPerPage();
    const start = (currentPage - 1) * itemsPerPage;
    return products.slice(start, start + itemsPerPage);
  };

  const getTotalPages = () => {
    return Math.ceil(products.length / getItemsPerPage());
  };

  // 페이지 나누기
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


  // 페이지 스크롤
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
            <div key={product.prodId} className="productCard">
              <img
                className="prodImg"
                src={`http://localhost:8081/images/${product.prodImg}.jpg`}
                alt="상품 이미지"
              />
              <button
                className="detailBtn"
                onClick={() => {
                  const userId = localStorage.getItem("userId");
                  navigate(`/proddetail/${product.prodId}?userId=${userId}`);
                }}
              >
                Detail<span className="detailBtnArrow">→</span>
              </button>
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
            <img src="/imgs/arrow.png" alt="위로" className="arrowImg arrowUp" />
          </button>

          <button className="navArrow down" onClick={scrollToBottom}>
            <img src="/imgs/arrow.png" alt="아래로" className="arrowImg arrowDown" />
          </button>

        </div>
      </div>
    </div>
  );
};

export default AllProd;