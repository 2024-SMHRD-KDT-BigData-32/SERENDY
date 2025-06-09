import { useState } from 'react';
import '../css/ProdDetail.css';

const ProdDetail = () => {

    const productData = {
        style: {
            style: '밀리터리',
            subStyle: '스트리트',
        },
        outer: {
            기장: '롱',
            카테고리: '점퍼',
            디테일: ['퍼트리밍'],
            소재: ['패딩'],
            프린트: ['무지'],
            핏: '루즈',
        },
    };

    
    const [selected, setSelected] = useState(null);

    const handleSelect = (type) => {
        setSelected(type === selected ? null : type);
    };

    const { style, outer } = productData;

  return (
    <div className="pdContainer">
        <div className="prodDetailImg" />
        <div className="prodDetailInfo">
            <div className="pdTitleRow">
                <h2>
                    {style.style}. {style.subStyle}
                </h2>
                <div className="likeDislikeBtn">
                    <button
                        className={selected === 'like' ? 'selected' : ''}
                        onClick={() => handleSelect('like')}
                    >
                        <img src="/imgs/signLike.png" alt="좋아요버튼" className='likeBtn' />
                    </button>
                    <button
                        className={selected === 'dislike' ? 'selected' : ''}
                        onClick={() => handleSelect('dislike')}
                    >
                        <img src="/imgs/signDislike.png" alt="싫어요버튼" className='dislikeBtn' />
                    </button>
                </div>
            </div>

            {Object.entries(outer).map(([key, value]) => (
                <div className="attributeRow" key={key}>
                    <div>{key}</div>
                    <div className="pdDots" />
                    <div>{Array.isArray(value) ? value.join(', ') : value}</div>
                </div>
            ))}
            
        </div>
    </div>
  )
}

export default ProdDetail