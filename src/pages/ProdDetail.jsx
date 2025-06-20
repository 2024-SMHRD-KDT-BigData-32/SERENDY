import { useEffect, useRef, useState } from 'react';
import '../css/ProdDetail.css';
import { Link, useLocation, useParams } from 'react-router-dom';
import axios from 'axios';

const ProdDetail = () => {
  const { id } = useParams();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const userId = queryParams.get("userId");

  const [productData, setProductData] = useState(null);
  const [selected, setSelected] = useState(null);
  const initialFeedbackRef = useRef(null);
  const latestSelectedRef = useRef(null);

  const [simProducs, setSimProducs] = useState([])

  const handleSelect = (type) => {
    setSelected(prev => {
      const newVal = prev === type ? null : type;
      latestSelectedRef.current = newVal;
      return newVal;
    });
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const productRes = await axios.get(`/api/products/${id}`, {
          params: { userId }
        });
        setProductData(productRes.data.product)
        console.log(productRes.data.product)
        setSimProducs(productRes.data.similar);
        console.log(productRes.data.similar)

        const feedbackRes = await axios.get(`/api/products/feedbackStatus`, {
          params: {
            id: userId,
            prodId: id
          }
        });

        const feedbackType = feedbackRes.data;
        if (feedbackType === 'LIKE' || feedbackType === 'DISLIKE') {
          setSelected(feedbackType);
          initialFeedbackRef.current = feedbackType;
          latestSelectedRef.current = feedbackType;
        } else {
          setSelected(null);
          initialFeedbackRef.current = null;
          latestSelectedRef.current = null;
        }

      } catch (error) {
        console.error("상품 상세 또는 피드백 상태 조회 실패:", error);
      }
    };

    if (id && userId) {
      fetchData();
    }
  }, [id, userId]);

  useEffect(() => {
    const handleBeforeUnload = (e) => {
      const initial = initialFeedbackRef.current ?? 'NONE';
      const current = latestSelectedRef.current ?? 'NONE';

      if (current !== initial) {
        const payload = {
          id: userId,
          prodId: parseInt(id),
          fbType: current
        };
        const blob = new Blob([JSON.stringify(payload)], { type: 'application/json' });
        navigator.sendBeacon('/api/products/submitFeedback', blob);
      }
    };

    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, [userId, id]);

  useEffect(() => {
    return () => {
      const initial = initialFeedbackRef.current ?? 'NONE';
      const current = latestSelectedRef.current ?? 'NONE';

      if (current !== initial) {
        axios.post('/api/products/submitFeedback', {
          id: userId,
          prodId: parseInt(id),
          fbType: current
        }).then(() => {
          console.log('페이지 이동 - 피드백 저장');
        }).catch(err => {
          console.error('피드백 저장 실패:', err);
        });
      }
    };
  }, [userId, id]);

  if (!productData) return <div>Loading...</div>;

  const { prodCate, color, detail, fit, length, material, print } = productData;

  const attributes = {
    카테고리: prodCate,
    색상: color,
    디테일: detail,
    핏: fit,
    기장: length,
    소재: material,
    프린트: print
  };

  return (
    <>
      <div className="pdContainer">
        <img src={`http://localhost:8081/images/${productData.prodImg}.jpg`} alt="상품 이미지" className="prodDetailImg" />
        <div className="prodDetailInfo">
          <div className="pdTitleRow">
            <h2>Style.
              <span> {productData.styleCode}</span>
            </h2>
            <div className="likeDislikeBtn">
              <button
                className={selected === 'LIKE' ? 'selected' : ''}
                onClick={() => handleSelect('LIKE')}
              >
                <img src="/imgs/signLike.png" alt="좋아요" className="likeBtn" />
              </button>
              <button
                className={selected === 'DISLIKE' ? 'selected' : ''}
                onClick={() => handleSelect('DISLIKE')}
              >
                <img src="/imgs/signDislike.png" alt="싫어요" className="dislikeBtn" />
              </button>
            </div>
          </div>

          {Object.entries(attributes)
            .filter(([_, value]) => value !== '')
            .map(([key, value]) => (
              <div className="attributeRow" key={key}>
                <div>{key}</div>
                <div className="pdDots" />
                <div>{value}</div>
              </div>
            ))}
        </div>

      </div>

      {simProducs.length > 0 && (
        <div className="similarSection">
          <h3 className="similarTitle">이 상품을 본 고객들이 함께 본 상품</h3>
          <div className="similarList">
            {simProducs.map((prod) => (
              <div key={prod.prodId} className="similarItem">
                <Link to={`/proddetail/${prod.prodId}?userId=${userId}`} className="hoverOverlayWrapper">
                  <img
                    src={`http://localhost:8081/images/${prod.prodImg}.jpg`}
                    alt={`상품 ${prod.prodId}`}
                    className="similarImg"
                  />
                  <div className="hoverOverlay">
                    <span className="hoverText">Detail →</span>
                  </div>
                </Link>
              </div>
            ))}
          </div>
        </div>
      )}

    </>
  );
};

export default ProdDetail;