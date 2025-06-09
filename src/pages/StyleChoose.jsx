import { useState } from 'react'
import '../css/StyleChoose.css'

const StyleChoose = () => {
  const [selectedStyles, setSelectedStyles] = useState([])

  const styleData = [
    {
      name: "트래디셔널",
      description: "고전적인 실루엣과 정제된 분위기를\n중시하는 전통적인 스타일",
      image: "/imgs/styleImgs/트래디셔널.jpg"
    },
    {
      name: "매니시",
      description: "남성적인 디테일과 실루엣을 활용한\n중성적이면서 세련된 스타일",
      image: "/imgs/styleImgs/매니시.jpg"
    },
    {
      name: "페미닌",
      description: "우아하고 부드러운 분위기를 강조한\n여성스러운 스타일",
      image: "/imgs/styleImgs/페미닌.jpg"
    },
    {
      name: "에스닉",
      description: "민족적, 전통적 패턴과 색감을 활용한\n이국적인 분위기의 스타일",
      image: "/imgs/styleImgs/에스닉.jpg"
    },
    {
      name: "컨템포러리",
      description: "현대적이고 세련된 감각의\n실용적인 스타일",
      image: "/imgs/styleImgs/컨템포러리.jpg"
    },
    {
      name: "내추럴",
      description: "편안하고 자연스러운 소재와 색감으로\n구성된 부드러운 스타일",
      image: "/imgs/styleImgs/내추럴.jpg"
    },
    {
      name: "젠더플루이드",
      description: "성별 구분을 넘나드는\n자유롭고 유니크한 스타일",
      image: "/imgs/styleImgs/젠더플루이드.jpg"
    },
    {
      name: "스포티",
      description: "운동복 기반의 활동적인 디자인으로\n편안하고 역동적인 스타일",
      image: "/imgs/styleImgs/스포티.jpg"
    },
    {
      name: "서브컬쳐",
      description: "음악, 예술, 스트리트 감성을 반영한\n개성 강한 스타일",
      image: "/imgs/styleImgs/서브컬쳐.jpg"
    },
    {
      name: "캐주얼",
      description: "일상에서 편하게 입을 수 있는\n자유롭고 실용적인 스타일",
      image: "/imgs/styleImgs/캐주얼.jpg"
    },
  ]

  const handleStyleSelect = (styleName) => {
    if (selectedStyles.includes(styleName)) {
      setSelectedStyles(selectedStyles.filter(name => name !== styleName))
    } else {
      if (selectedStyles.length < 3) {
        setSelectedStyles([...selectedStyles, styleName])
      } else {
        alert("최대 3개까지 선택할 수 있습니다.")
      }
    }
  }

  const handleComplete = () => {
    if (selectedStyles.length > 0) {
      alert(`선택된 스타일: ${selectedStyles.join(', ')}`)
    } else {
      alert("최소 하나의 스타일을 선택해주세요.")
    }
  }

  return (
    <div>
      <div className="styleTop">
        <h1>Choose a style</h1>
        <hr />
        <p>원하는 스타일을 선택해주세요.</p>
      </div>

      <div className="stylesContainer">
        {styleData.map((style) => (
          <div
            key={style.name}
            className={`styleCard ${selectedStyles.includes(style.name) ? 'selected' : ''}`}
            onClick={() => handleStyleSelect(style.name)}
          >
            {/* <div className="imagePlaceholder"></div> */}
            <img src={style.image} alt={style.name} className='styleImg' />
            <div className="styleName">{style.name}</div>
            <div className="styleDescription">
              {style.description.split('\n').map((line, index) => (
                <div key={index}>{line}</div>
              ))}
            </div>
          </div>
        ))}
      </div>

      <button className="completeBtn" onClick={handleComplete}>
        선택완료
      </button>
    </div>
  )
}

export default StyleChoose