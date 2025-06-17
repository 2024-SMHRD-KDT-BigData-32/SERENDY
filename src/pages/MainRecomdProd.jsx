import { useState, useEffect } from "react"
import "../css/MainRecomdProd.css"
import axios from "axios"
import { Link } from "react-router-dom"

const MainRecomdProd = () => {
  const [currentIndex, setCurrentIndex] = useState(0)
  const [isAutoPlaying, setIsAutoPlaying] = useState(true)
  const [isTransitioning, setIsTransitioning] = useState(true)

  const userId = localStorage.getItem('userId');
  const [isLoading, setIsLoading] = useState(true);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get(`/api/recommend/${userId}`);
        const allProducts = res.data;

        // 배열 셔플 (Fisher–Yates 알고리즘)
        const shuffled = [...allProducts].sort(() => 0.5 - Math.random());

        // 앞에서 12개만 잘라내기
        const selected = shuffled.slice(0, 12);
        setProducts(selected);
        console.log(selected);
      } catch (err) {
        console.error('추천 상품 로딩 실패:', err);
      } finally {
        setIsLoading(false);
      }
    };

    if (userId) {
      fetchData();
    }
  }, [userId]);

  const totalproducts = products.length
  const productsPerMove = 3
  const totalPages = Math.ceil(totalproducts / productsPerMove)

  // 무한 루프를 위한 확장된 아이템 배열
  const extendedproducts = [
    ...products.slice(-6), // 마지막 6개 복제
    ...products, // 원본 12개
    ...products.slice(0, 6), // 처음 6개 복제
  ]

  // 현재 페이지 계산 (정규화된 인덱스 사용)
  const normalizedIndex = ((currentIndex % totalproducts) + totalproducts) % totalproducts
  const currentPage = Math.floor(normalizedIndex / productsPerMove)
  const progress = Math.min(100, Math.max(0, (currentPage / (totalPages - 1)) * 100))

  // 다음 슬라이드
  const handleNext = () => {
    if (currentIndex >= totalproducts - productsPerMove) {
      // 마지막 페이지에서 다음으로 갈 때
      setCurrentIndex(currentIndex + productsPerMove) // 복제된 영역으로 이동
    } else {
      setCurrentIndex(currentIndex + productsPerMove)
    }
  }

  // 이전 슬라이드
  const handlePrev = () => {
    if (currentIndex <= 0) {
      // 첫 페이지에서 이전으로 갈 때
      setCurrentIndex(currentIndex - productsPerMove) // 복제된 영역으로 이동
    } else {
      setCurrentIndex(currentIndex - productsPerMove)
    }
  }

  // 무한 루프 리셋 처리
  useEffect(() => {
    if (currentIndex >= totalproducts) {
      // 마지막을 넘어선 경우
      const timer = setTimeout(() => {
        setIsTransitioning(false)
        setCurrentIndex(0)
        setTimeout(() => setIsTransitioning(true), 50)
      }, 700) // transition 시간과 동일

      return () => clearTimeout(timer)
    } else if (currentIndex < 0) {
      // 첫 번째를 넘어선 경우
      const timer = setTimeout(() => {
        setIsTransitioning(false)
        setCurrentIndex(totalproducts - productsPerMove)
        setTimeout(() => setIsTransitioning(true), 50)
      }, 700) // transition 시간과 동일

      return () => clearTimeout(timer)
    }
  }, [currentIndex, totalproducts, productsPerMove])

  // 자동 재생
  useEffect(() => {
    if (!isAutoPlaying) return

    const interval = setInterval(handleNext, 4000)
    return () => clearInterval(interval)
  }, [isAutoPlaying, currentIndex])

  const translateX = -((currentIndex + 6 - 3) * 20);

  return (
    <div className="mrpContainer">
      <div className="mrpTitle">Recommend Items for You</div>
      <div className="mrpSubtitle">선택하신 스타일 정보 기반으로 추천된 상품입니다.</div>

      {isLoading ? (
        <div className="loadText">상품을 불러오는 중입니다...</div>
      ) : extendedproducts.length === 0 ? (
        <div className="loadText">추천된 상품이 없습니다.</div>
      ) : (
        <>
          <div className="carouselWrapper">
            <Link to="/allrecomdprod" className='viewAll'>
              <span>더보기</span>
            </Link>
            <div
              className={`sliderWrapper ${isTransitioning ? "transitioning" : ""}`}
              style={{ transform: `translateX(${translateX}%)` }}
            >
              {extendedproducts.map((item, index) => {
                const isActive = index === currentIndex + 6; // 현재 중앙 슬라이드
                return (
                  <div key={`${item.prodId}-${index}`} className={`carouselItem ${isActive ? 'active' : ''}`}>
                    <div className="itemImg">
                      <img src={`http://localhost:8081/images/${item.prodImg}.jpg`} alt={`Item ${item.prodId}`} />
                      <Link to={`/proddetail/${item.prodId}?userId=${userId}`} className="hoverOverlay">
                        <span className="hoverText">Detail →</span>
                      </Link>
                    </div>
                  </div>
                );
              })}

            </div>
          </div>

          <div className="controlSection">
            <div className="progressTrack">
              <div className="progressBar" style={{ width: `${progress}%` }} />
            </div>

            <div className="controlBtns">
              <button onClick={handlePrev} className="controlBtn" aria-label="Previous slide">
                <img className="leftArrow" src="/imgs/simpleArrow.png" alt="왼쪽화살표" />
              </button>

              <button onClick={handleNext} className="controlBtn" aria-label="Next slide">
                <img className="rightArrow" src="/imgs/simpleArrow.png" alt="오른쪽화살표" />
              </button>

              <button
                onClick={() => setIsAutoPlaying(!isAutoPlaying)}
                className="controlBtn"
                aria-label={isAutoPlaying ? "Pause slideshow" : "Play slideshow"}
              >
                {isAutoPlaying ?
                  <img className="controlImg" src="/imgs/pause.png" alt="정지" />
                  : <img className="controlImg" src="/imgs/play.png" alt="재생" />
                }
              </button>
            </div>
          </div>
        </>
      )}
    </div>

  )
}

export default MainRecomdProd