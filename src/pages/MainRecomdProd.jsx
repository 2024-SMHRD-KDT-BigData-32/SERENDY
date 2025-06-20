import axios from 'axios';
import '../css/MainRecomdProd.css';
import { motion } from 'framer-motion';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const fadeUp = {
  hidden: { opacity: 0, y: 20 },
  visible: {
    opacity: 1,
    y: 0,
    transition: {
      duration: 0.6,
      ease: 'easeOut'
    }
  }
};

const container = {
  hidden: {},
  visible: {
    transition: {
      staggerChildren: 0.2
    }
  }
};

const MainRecomdProd = () => {
  const userId = localStorage.getItem('userId');
  const [userStyle, setUserStyle] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [allRecomdProd, setAllRecomdProd] = useState([]);
  const [styleRecomdProd, setStyleRecomdProd] = useState([]);
  const [clickRecomdProd, setClickRecomdProd] = useState([]);
  const [trendRecomdProd, setTrendRecomdProd] = useState([]);

  useEffect(() => {
    const fetchAll = async () => {
      try {
        const [allRes, styleRes, clickRes, trendRes, userStyleRes] = await Promise.all([
          axios.get(`/api/recommend/${userId}`),
          axios.post(`/api/recommend/cbf/all?userId=${userId}`),
          axios.post(`/api/recommend/ctrAll?userId=${userId}`),
          axios.get('/api/recommend/trend/all'),
          axios.get(`/api/users/getStylePref/${userId}`)
        ]);

        const allData = allRes.data;
        const styleData = styleRes.data;
        const clickData = clickRes.data;
        const trendData = trendRes.data;

        const usedProdIds = new Set();

        const pickUniqueItems = (sourceList, count) => {
          const result = [];
          for (let item of sourceList) {
            if (!usedProdIds.has(item.prodId)) {
              result.push(item);
              usedProdIds.add(item.prodId);
            }
            if (result.length === count) break;
          }
          return result;
        };

        const selectedAll = pickUniqueItems(allData, 3);
        const selectedStyle = pickUniqueItems(styleData, 3);
        const selectedClick = pickUniqueItems(clickData, 3);

        const trendCandidates = pickUniqueItems(trendData, trendData.length);
        const selectedTrend = trendCandidates.sort(() => Math.random() - 0.5).slice(0, 3);

        setAllRecomdProd(selectedAll);
        setStyleRecomdProd(selectedStyle);
        setClickRecomdProd(selectedClick);
        setTrendRecomdProd(selectedTrend);
        setUserStyle(userStyleRes.data);

        console.log(styleRes.data)
        console.log(clickRes.data)

      } catch (err) {
        console.error('에러 정보', err);
      } finally {
        setIsLoading(false);
      }
    };

    if (userId) fetchAll();
  }, [userId]);

  return (
    <div className="scrollWrapper">
      <div className="mrpTitle">Recommend Items for You</div>
      {isLoading ? (
        <div className="loadingBox">
          <p>상품을 불러오는 중입니다...</p>
        </div>
      ) : (
        <>
          {/* Best for You */}
          {allRecomdProd.length === 3 && (
            <motion.section className="scrollSection bestSection" variants={container} initial="hidden" whileInView="visible" viewport={{ once: true }}>
              <motion.h2 className="sectionTitle" variants={fadeUp}>Best for You</motion.h2>
              <motion.p className="sectionDesc" variants={fadeUp}>당신에게 가장 적합한 상품이에요.</motion.p>
              <motion.div className="bestImageRow" variants={container}>
                {allRecomdProd.map((item) => (
                  <motion.div key={item.prodId} className="imgCard" variants={fadeUp}>
                    <Link to={`/proddetail/${item.prodId}?userId=${userId}`} className="hoverOverlayWrapper">
                      <img src={`http://localhost:8081/images/${item.prodImg}.jpg`} alt={item.prodId} />
                      <div className="hoverOverlay">
                        <span className="hoverText">Detail →</span>
                      </div>
                    </Link>
                  </motion.div>
                ))}
                {/* 더보기 버튼 */}
                <motion.div className="moreLinkWrapper" variants={fadeUp}>
                  <Link to="/allrecomdprod" className="moreLink">더보기＋</Link>
                </motion.div>
              </motion.div>
            </motion.section>
          )}

          {/* Based on Your Style */}
          {styleRecomdProd.length === 3 && (
            <motion.section className="scrollSection" variants={container} initial="hidden" whileInView="visible" viewport={{ once: true, amount: 0.5 }}>
              <motion.h2 className="sectionTitle" variants={fadeUp}>Based on Your Style</motion.h2>
              <motion.p className="sectionDesc" variants={fadeUp}>
                "{userStyle.join(', ')}" 스타일을 반영한 추천 상품이에요.
              </motion.p>
              <motion.div className="imageGrid" variants={container}>
                {styleRecomdProd.map((item) => (
                  <motion.div key={item.prodId} className="imgCard" variants={fadeUp}>
                    <Link to={`/proddetail/${item.prodId}?userId=${userId}`} className="hoverOverlayWrapper">
                      <img src={`http://localhost:8081/images/${item.prodImg}.jpg`} alt={item.prodId} />
                      <div className="hoverOverlay">
                        <span className="hoverText">Detail →</span>
                      </div>
                    </Link>
                  </motion.div>
                ))}
              </motion.div>
            </motion.section>
          )}

          {/* You May Like */}
          {clickRecomdProd.length === 3 && (
            <motion.section className="scrollSection" variants={container} initial="hidden" whileInView="visible" viewport={{ once: true, amount: 0.5 }}>
              <motion.h2 className="sectionTitle" variants={fadeUp}>You May Like</motion.h2>
              <motion.p className="sectionDesc" variants={fadeUp}>당신이 좋아할 만한 상품을 모아봤어요.</motion.p>
              <motion.div className="imageGrid" variants={container}>
                {clickRecomdProd.map((item) => (
                  <motion.div key={item.prodId} className="imgCard" variants={fadeUp}>
                    <Link to={`/proddetail/${item.prodId}?userId=${userId}`} className="hoverOverlayWrapper">
                      <img src={`http://localhost:8081/images/${item.prodImg}.jpg`} alt={item.prodId} />
                      <div className="hoverOverlay">
                        <span className="hoverText">Detail →</span>
                      </div>
                    </Link>
                  </motion.div>
                ))}
              </motion.div>
            </motion.section>
          )}

          {/* Weekly Trends */}
          {trendRecomdProd.length === 3 && (
            <motion.section className="scrollSection" variants={container} initial="hidden" whileInView="visible" viewport={{ once: true, amount: 0.5 }}>
              <motion.h2 className="sectionTitle" variants={fadeUp}>Weekly Trends</motion.h2>
              <motion.p className="sectionDesc" variants={fadeUp}>이번 주 트렌드를 반영한 인기 상품이에요.</motion.p>
              <motion.div className="imageGrid" variants={container}>
                {trendRecomdProd.map((item) => (
                  <motion.div key={item.prodId} className="imgCard" variants={fadeUp}>
                    <Link to={`/proddetail/${item.prodId}?userId=${userId}`} className="hoverOverlayWrapper">
                      <img src={`http://localhost:8081/images/${item.prodImg}.jpg`} alt={item.prodId} />
                      <div className="hoverOverlay">
                        <span className="hoverText">Detail →</span>
                      </div>
                    </Link>
                  </motion.div>
                ))}
              </motion.div>
            </motion.section>
          )}
        </>
      )}
    </div>
  );
};

export default MainRecomdProd;