import { useEffect, useState } from 'react';
import '../css/MainRecomdProd.css';
import { Link } from 'react-router-dom';

const categories = ['#TOP', '#OUTER', '#BOTTOM', '#DRESS'];

const dummyImages = {
  '#TOP': ['top1.png', 'top2.png', 'top3.png', 'top4.png', 'top5.png'],
  '#OUTER': ['outer1.png', 'outer2.png', 'outer3.png', 'outer4.png', 'outer5.png'],
  '#BOTTOM': ['bottom1.png', 'bottom2.png', 'bottom3.png', 'bottom4.png', 'bottom5.png'],
  '#DRESS': ['dress1.png', 'dress2.png', 'dress3.png', 'dress4.png', 'dress5.png'],
};

const RecommendItems = () => {
  const [activeTab, setActiveTab] = useState('#TOP');

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveTab((prev) => {
        const currentIndex = categories.indexOf(prev);
        return categories[(currentIndex + 1) % categories.length];
      });
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const currentImages = dummyImages[activeTab];

  return (
    <div className="mrpContainer">

      <div className="mrpTitle">이름님을 위한 추천 아이템</div>
      <div className="mrpSubtitle">선택하신 스타일 정보 기반으로 추천됩니다.</div>

      <div>
      
        <div className="recomdTabGroup">
            {categories.map((cat, idx) => {
                const isFirst = idx === 0;
                const isLast = idx === categories.length - 1;
                const isActive = activeTab === cat;

                const tabClass = `
                recomdTab
                ${isActive ? 'active' : ''}
                ${isActive && isFirst ? 'first' : ''}
                ${isActive && isLast ? 'last' : ''}
                `;

                return (
                <button
                    key={cat}
                    className={tabClass.trim()}
                    onClick={() => setActiveTab(cat)}
                >
                    {cat}
                </button>
                );
            })}
        </div>

      </div>

      <div className="recomdImg">

        <div className="largeImg hoverBox">
          {currentImages[0]}
          <Link to="" className="hoverOverlay">
            <span className="hoverText">Detail →</span>
          </Link>
        </div>

        <div className="imgGrid">
          {currentImages.slice(1).map((imgName, idx) => (
            <div key={idx} className="smallImg hoverBox">
              {imgName}
              <Link to="" className="hoverOverlay">
                <span className="hoverText">Detail →</span>
              </Link>
            </div>
          ))}
        </div>

        <Link to={`/allrecomdprod?category=${encodeURIComponent(activeTab.slice(1))}`} className='viewAll'>
          <span>모든 추천 상품 보러가기</span>
          <img src="/imgs/Vector.png" className='mrpVectorImg' />
        </Link>
        
      </div>

      
    </div>
  );
};

export default RecommendItems;
