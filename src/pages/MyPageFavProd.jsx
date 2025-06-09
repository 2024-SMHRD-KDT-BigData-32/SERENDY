import { useState } from 'react'
import '../css/AllRecomdProd.css'
import { Link } from 'react-router-dom'

const MyPageFavProd = () => {
    // 카테고리별 상품 데이터 생성 (더 많은 상품으로 증가)
    const allProducts = [
        // 상의 카테고리 상품 (30개)
        ...Array(30)
        .fill(null)
        .map((_, index) => ({
            id: index + 1,
            image: "/placeholder.svg?height=300&width=250",
            category: "상의",
        })),

        // 아우터 카테고리 상품 (25개)
        ...Array(25)
        .fill(null)
        .map((_, index) => ({
            id: index + 31,
            image: "/placeholder.svg?height=300&width=250",
            category: "아우터",
        })),

        // 하의 카테고리 상품 (28개)
        ...Array(28)
        .fill(null)
        .map((_, index) => ({
            id: index + 56,
            image: "/placeholder.svg?height=300&width=250",
            category: "하의",
        })),

        // 원피스 카테고리 상품 (22개)
        ...Array(22)
        .fill(null)
        .map((_, index) => ({
            id: index + 84,
            image: "/placeholder.svg?height=300&width=250",
            category: "원피스",
        })),
    ]

    // 상태 관리
    const [gridType, setGridType] = useState("4x4")
    const [activeCategory, setActiveCategory] = useState("상의")
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
        setCurrentPage(1) // 그리드 타입 변경 시 첫 페이지로 이동
    }

    // 현재 카테고리의 모든 상품 가져오기
    const getCategoryProducts = () => {
        return allProducts.filter((product) => product.category === activeCategory)
    }

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

    // 페이지 변경 핸들러
    const handlePageChange = (page) => {
        setCurrentPage(page)
        // 페이지 변경 시 상단으로 스크롤
        window.scrollTo({ top: 0, behavior: "smooth" })
    }

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
            <div className="arpTitle">이름님의 선호 상품</div>

            {/* 카테고리 */}
            <nav className="arpCategoryNav">

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
            {getCurrentPageProducts().map((product) => (
                <div key={product.id} className="productCard">
                    <img className="prodImg" src={product.image} alt="상품 이미지" />
                    <Link to="" className="detailBtn">
                    {/* to={`/product/${product.id}`} */}
                        Detail
                        <span className="detailBtnArrow">→</span>
                    </Link>
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
                    <img src="/imgs/화살표.png" alt="위로" className="arrowImg arrowUp" />
                </button>

                <button className="navArrow down" onClick={scrollToBottom}>
                    <img src="/imgs/화살표.png" alt="아래로" className="arrowImg arrowDown" />
                </button>

            </div>
            
        </div>
    </div>
  )
}

export default MyPageFavProd