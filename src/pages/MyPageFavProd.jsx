import { useEffect, useState } from 'react'
import '../css/AllRecomdProd.css'
import { useNavigate } from 'react-router-dom'
import axios from 'axios';

const categoryData = {
  "상의": ["탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티"],
  "하의": ["청바지", "팬츠", "스커트", "레깅스", "조거팬츠"],
  "아우터": ["코트", "재킷", "점퍼", "패딩", "베스트", "가디건", "짚업"],
  "원피스": ["드레스", "점프수트"]
};

const MyPageFavProd = () => {

  const navigate = useNavigate()
  const userId = localStorage.getItem("userId")
  const [products, setProducts] = useState([])

  useEffect(() => {
    if (userId) {
      axios.get(`/api/products/likeList/${userId}`)
        .then(res => {
          setProducts(res.data)
          console.log(res.data)
        })
        .catch(err => {
          console.error("좋아요 상품 불러오기 실패:", err)
        })
    }
  }, [userId])

  const [edit, setEdit] = useState(false);
  const [showEditBtn, setShowEditBtn] = useState(true);
  const [selectedProducts, setSelectedProducts] = useState([]);

  const toggleProductSelection = (prodId) => {
    setSelectedProducts((prev) =>
      prev.includes(prodId)
        ? prev.filter((id) => id !== prodId)
        : [...prev, prodId]
    );
  };

  const handleDeleteSelected = async () => {
    if (selectedProducts.length === 0) return;

    const confirmed = window.confirm("선택한 상품들의 좋아요를 모두 취소할까요?");
    if (!confirmed) return;

    try {
      await Promise.all(
        selectedProducts.map((prodId) =>
          axios.post('/api/products/submitFeedback', {
            id: userId,
            prodId: prodId,
            fbType: 'NONE'
          })
        )
      );

      // UI에서 제거
      setProducts((prev) =>
        prev.filter((product) => !selectedProducts.includes(product.prodId))
      );
      setSelectedProducts([]);
      alert("좋아요가 모두 취소되었습니다.");
    } catch (error) {
      console.error("삭제 실패:", error);
      alert("일부 상품의 좋아요 취소에 실패했습니다.");
    }
  };

  const handleCompleteEdit = () => {
    setEdit(false);
    setShowEditBtn(false);

    setTimeout(() => {
      setShowEditBtn(true);
    }, 200);
  };

  // 상태 관리
  const [gridType, setGridType] = useState("4x4")
  const [activeCategory, setActiveCategory] = useState(null)
  const [currentPage, setCurrentPage] = useState(1)

  // 그리드 타입에 따른 페이지당 상품 개수
  const getItemsPerPage = () => {
    switch (gridType) {
      case "3x3":
        return 9
      case "4x4":
        return 16
      case "5x5":
        return 25
      default:
        return 9
    }
  }

  // 카테고리 변경 핸들러
  const handleCategoryChange = (category) => {
    setActiveCategory(category)
    setCurrentPage(1) // 카테고리 변경 시 첫 페이지로 이동
  }

  // 그리드 타입 변경 핸들러
  const handleGridTypeChange = (type) => {
    setGridType(type)
  }

  // 현재 카테고리의 모든 상품 가져오기
  const getCategoryProducts = () => {
    if (!activeCategory) return products;
    const subCategories = categoryData[activeCategory] || [];
    return products.filter(product => subCategories.includes(product.prodCate?.trim()));
  };

  // 현재 페이지에 표시할 상품들 가져오기
  const getCurrentPageProducts = () => {
    const categoryProducts = getCategoryProducts()
    const itemsPerPage = getItemsPerPage()
    const startIndex = (currentPage - 1) * itemsPerPage
    const endIndex = startIndex + itemsPerPage
    return categoryProducts.slice(startIndex, endIndex)
  }

  // 총 페이지 수 계산
  const getTotalPages = () => {
    const categoryProducts = getCategoryProducts()
    const itemsPerPage = getItemsPerPage()
    return Math.ceil(categoryProducts.length / itemsPerPage)
  }

  // 페이지 변경 핸들러
  const handlePageChange = (page) => {
    setCurrentPage(page)
    // 페이지 변경 시 상단으로 스크롤
    window.scrollTo({ top: 0, behavior: "smooth" })
  }

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

  // 페이지 상단으로 스크롤
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    })
  }

  // 페이지 하단으로 스크롤
  const scrollToBottom = () => {
    window.scrollTo({
      top: document.documentElement.scrollHeight,
      behavior: "smooth",
    })
  }

  return (

    <div className="arpFrame">
      <div className="arpContainer">
        <div className="arpTitle">Your Favorite Items</div>

        {/* 카테고리 버튼 */}
        <nav className="arpCategoryNav">
          <span
            className={`arpCategoryTab ${activeCategory === null ? "active" : ""}`}
            onClick={() => handleCategoryChange(null)}
          >
            전체
          </span>

          <span className="arpCategoryDivider">|</span>

          <span
            className={`arpCategoryTab ${activeCategory === "상의" ? "active" : ""}`}
            onClick={() => handleCategoryChange("상의")}
          >
            상의
          </span>

          <span className="arpCategoryDivider">|</span>

          <span
            className={`arpCategoryTab ${activeCategory === "아우터" ? "active" : ""}`}
            onClick={() => handleCategoryChange("아우터")}
          >
            아우터
          </span>

          <span className="arpCategoryDivider">|</span>

          <span
            className={`arpCategoryTab ${activeCategory === "하의" ? "active" : ""}`}
            onClick={() => handleCategoryChange("하의")}
          >
            하의
          </span>

          <span className="arpCategoryDivider">|</span>

          <span
            className={`arpCategoryTab ${activeCategory === "원피스" ? "active" : ""}`}
            onClick={() => handleCategoryChange("원피스")}
          >
            원피스
          </span>

        </nav>

        {/* 그리드 버튼 */}
        <div className="gridBtn">

          <div
            className={`gridIcon ${gridType === "3x3" ? "active" : ""}`}
            onClick={() => handleGridTypeChange("3x3")}
          >
            <img src="/imgs/33grid.png" alt="3열 그리드" className="gridImg" />
          </div>

          <div
            className={`gridIcon ${gridType === "4x4" ? "active" : ""}`}
            onClick={() => handleGridTypeChange("4x4")}
          >
            <img src="/imgs/44grid.png" alt="4열 그리드" className="gridImg" />
          </div>

          <div
            className={`gridIcon ${gridType === "5x5" ? "active" : ""}`}
            onClick={() => handleGridTypeChange("5x5")}
          >
            <img src="/imgs/55grid.png" alt="5열 그리드" className="gridImg" />
          </div>

        </div>



        {/* 상품 목록 */}
        <div className={`productsGrid grid-${gridType}`}>
          {/* 편집 버튼 */}
          <div className="editContainer">
            {!edit && showEditBtn && (
              <button className={`editBtn ${!edit ? 'fadeIn' : 'fadeOut'}`} onClick={() => setEdit(true)}>
                편집
              </button>
            )}

            <div className={`editControls ${edit ? 'active' : ''}`}>
              <button
                className="selectAllBtn"
                onClick={() => {
                  const visibleProductIds = getCurrentPageProducts().map(p => p.prodId);
                  setSelectedProducts(prev => {
                    const newSet = new Set([...prev, ...visibleProductIds]);
                    return Array.from(newSet);
                  });
                }}
              >
                전체선택
              </button>

              <span className="arpCategoryDivider">|</span>

              <button
                className="deselectAllBtn"
                onClick={() => {
                  const visibleProductIds = getCurrentPageProducts().map(p => p.prodId);
                  setSelectedProducts(prev =>
                    prev.filter(id => !visibleProductIds.includes(id))
                  );
                }}
                disabled={selectedProducts.length === 0}
              >
                전체해제
              </button>

              <span className="arpCategoryDivider">|</span>

              <button
                className="deleteBtn"
                onClick={handleDeleteSelected}
                disabled={selectedProducts.length === 0}
              >
                선택 삭제
              </button>

              <span className="arpCategoryDivider">|</span>

              <button className="fpCompleteBtn" onClick={handleCompleteEdit}>
                완료
              </button>
            </div>
          </div>

          {products.length === 0 && (
            <div className="loadText">
              좋아요한 상품이 없습니다.
            </div>
          )}

          {getCurrentPageProducts().map((product) => (
            <div
              key={product.prodId}
              className={`productCard ${edit ? "editable" : ""} ${selectedProducts.includes(product.prodId) ? "selected" : ""
                }`}
              onClick={() => {
                if (edit) toggleProductSelection(product.prodId);
              }}
            >
              {edit && (
                <input
                  type="checkbox"
                  className="productCheckbox"
                  checked={selectedProducts.includes(product.prodId)}
                  onChange={() => toggleProductSelection(product.prodId)}
                  onClick={(e) => e.stopPropagation()}
                />
              )}

              <img
                className="prodImg"
                src={`http://localhost:8081/images/${product.prodImg}.jpg`}
                alt="상품 이미지"
              />

              <button
                className={`detailBtn ${edit ? "disabled" : ""}`}
                onClick={(e) => {
                  if (edit) return;
                  e.stopPropagation();
                  const userId = localStorage.getItem("userId");
                  navigate(`/proddetail/${product.prodId}?userId=${userId}`);
                }}
                disabled={edit}
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

        {/* 우측의 페이지 상하단 이동 버튼 */}
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
  )
}

export default MyPageFavProd