import { FaInstagram, FaFacebookF } from 'react-icons/fa';
import { SiX } from 'react-icons/si';
import '../css/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-inner">
        <div className="footer-left">
          <h2 className="footerLogo">Serendy</h2>
          <p className="copyright">© 2025 Serendy. All rights reserved.</p>
        </div>

        <div className="footer-center">
          <ul className="footer-menu">
            <li><a href="#">회사소개</a></li>
            <li><a href="#">이용약관</a></li>
            <li><a href="#">개인정보처리방침</a></li>
            <li><a href="#">고객센터</a></li>
          </ul>
        </div>

        <div className="footer-right">
          <div className="social-icons">
            <a href="#"><FaInstagram /></a>
            <a href="#"><FaFacebookF /></a>
            <a href="#"><SiX /></a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;